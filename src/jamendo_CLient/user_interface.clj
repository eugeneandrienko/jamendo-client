(ns jamendo-CLient.user-interface
  (:use [jamendo-CLient.jamendoapi-wrappers :only [get-paged-albums-apisafe]])
  (:import java.lang.String))

(declare list-of-commands)

(defn hello-msg []
  "Prints the welcome message"
  (println "Welcome to Jamendo client")
  (list-of-commands)
  (println))

(defn list-of-commands []
  "Print list of valid commands"
  (println)
  (println "You can use next commands:")
  (println "search-albums 'keyword'\t Search albums on the key 'keyword'")
  (println "list-album 'id'")
  (println "play-album 'id'")
  (println "play-songs 'id1 id2 ... idN'")
  (println "play-song 'id'")
  (println "list-commands")
  (println "quit"))

(defn prompt [prompt-str]
  "Print prompt and return user input as list"
  (defn string-to-list [string]
    (map (fn [x] x) (.split string " ")))
  (print prompt-str)
  (flush)
  (string-to-list (read-line)))

(defn print-search-albums-result [keyword]
  "Print result of search by 'keyword' on albums"
  (loop [page 1
         result (get-paged-albums-apisafe 10 page keyword)]
    (let
        [nextresult (get-paged-albums-apisafe 10 (+ page 1) keyword)]
      (if (not= result '{nil nil})
        (do
          (println result)
          (println)
          (println
           "Enter 'quit' to return to top-level or press Enter to continue")
          (if (= (first (prompt "> ")) "quit")
            :quit
            (recur (+ page 1)
                   nextresult)))))))
