(ns jamendo-CLient.user-interface
  (:import java.lang.String))

(defn hello-msg []
  "Prints the welcome message"
  (println "Welcome to Jamendo client")
  (println)
  (println "You can use next commands:")
  (println "search-albums 'keyword'\t Search albums on the key 'keyword'")
  (println "list-album 'id'")
  (println "play-album 'id'")
  (println "play-songs 'id1 id2 ... idN'")
  (println "play-song 'id'")
  (println "quit")
  (println))

(defn prompt []
  "Print prompt and return user input as list"
  (defn string-to-list [string]
    (map (fn [x] x) (.split string " ")))
  (print "=> ")
  (flush)
  (string-to-list (read-line)))

;; func - should be lambda function with one parameter - number
;; of requested page, which calls proper function with necessary
;; parameters.
(defn print-paged-list [func]
  "Prints paged list returned by 'get-paged-*' functions and
   return user input"
  (defn- print-paged-list-iter [func num]
    (. Thread sleep 1000)
    (let
        [requested-list (func num)]
      (cond
       (= requested-list '()) (println "End of list")
       :else (do
               (println requested-list)
               (println)
               (println "Press Enter to continue or enter the command")
               (let [user-input (prompt)]
                 (cond
                  (= user-input "") (print-paged-list-iter func
                                                             (+ num 1))
                  :else user-input))))))
  (print-paged-list-iter func 1))
