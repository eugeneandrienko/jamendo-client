(ns jamendo-CLient.user-interface
  (:use [jamendo-CLient.jamendoapi-wrappers :only [get-paged-albums-apisafe
                                                   get-album-songs
                                                   get-song]]))

(defn- list-to-string [mylist]
  "Converts list to string"
  (loop [result-str ""
         looplist mylist]
    (if (= (count looplist) 0)
      result-str
      (recur (str result-str (first looplist)) (next looplist)))))

(defn u-search-albums [keyword]
  "Print result of search by 'keyword' on albums"
  (loop [page 1
         result (get-paged-albums-apisafe 10 page keyword)]
    (let
        [nextresult (get-paged-albums-apisafe 10 (+ page 1) keyword)]
      (if (not= result '{nil nil})
        (do
          (println "ID\tAlbum name")
          (println "--\t----------")
          (println (map
                    (fn [x]
                      (str (x 0) "\t" (x 1) "\n")) result))
          (recur (+ page 1) nextresult))))))

(defn u-list-album [id]
  "Print list of songs in album with ID = 'id'"
  (println "ID\tSong name")
  (println "--\t---------")
  (println (map
            (fn [x] (str (x 0) "\t" ((x 1) 0) "\n"))
            (get-album-songs id))))

(defn u-print-album [id]
  "Play all songs in album with ID=id"
  (dorun
   (map
    (fn [x] (println ((x 1) 1)))
    (get-album-songs id))))

(defn u-print-song [id]
  (println (get-song id)))
