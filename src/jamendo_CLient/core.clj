(ns jamendo-CLient.core
  (:gen-class)
  (:require [clojure.contrib.str-utils2 :as sl])
  (:use [jamendo-CLient.user-interface :only [hello-msg
                                              list-of-commands
                                              prompt
                                              print-search-albums-result
                                              list-album
                                              play-album]]))

(defn process-user-commands []
  "Process user input and call proper function
   with transfer of command parameters to func"
  (loop [user-input (prompt "=> ")]
    (let [cmd (first user-input)]
      (cond
       (= cmd "search-albums") (print-search-albums-result
                                (sl/join " " (next user-input)))
       (= cmd "list-commands") (list-of-commands)
       (= cmd "list-album") (list-album (second user-input))
       (= cmd "play-album") (play-album (second user-input))
       :else (if (not= cmd "quit")
               (do (println "Unknown command!")
                   (list-of-commands))))
      (if (not= cmd "quit")
        (recur (prompt "=> "))))))

(defn -main [& args]
  (hello-msg)
  (process-user-commands))
