(ns jamendo-CLient.core
  (:gen-class)
  (:use [jamendo-CLient.jamendoapi-wrappers :only [get-paged-tags
                                                   get-paged-albums]]
        [jamendo-CLient.user-interface :only [hello-msg
                                              prompt]]))

(defn process-user-commands []
  (loop [cmd (first (prompt))]
    (cond
     (= nil (cond
             (= cmd "quit") nil
             (= cmd "search-albums") nil
             (= cmd "list-album") nil
             (= cmd "play-song") nil
             (= cmd "play-songs") nil
             (= cmd "play-album") nil
             :else (do
                     (println "Unknown command!")
                     :dumb-val))) nil
     :else (recur (first (prompt))))))

(defn -main [& args]
  (hello-msg)
  (process-user-commands))
