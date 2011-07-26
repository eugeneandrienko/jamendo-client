(ns jamendo-CLient.user-interface)

(defn- get-input []
  (print "=> ")
  (flush)
  (read-line))

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
               (let [user-input (get-input)]
                 (cond
                  (= user-input "") (print-paged-list-iter func
                                                             (+ num 1))
                  :else user-input))))))
  (print-paged-list-iter func 1))
