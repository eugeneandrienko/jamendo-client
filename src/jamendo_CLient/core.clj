(ns jamendo-CLient.core
  (:gen-class)
  (:use [jamendo-CLient.jamendoapi-wrappers :only [get-paged-tags
                                                   get-paged-albums]]
        [jamendo-CLient.user-interface :only [hello-msg
                                              list-of-commands
                                              prompt]]))

(defn process-user-commands []
  "Process user input and call proper function
   with transfer of command parameters to func"
  (loop [user-input (prompt)]
    (let [cmd (first user-input)]
      (cond
       (= cmd "search-albums") nil
       :else (if (not= cmd "quit")
               (do (println "Unknown command!")
                   (list-of-commands))))
      (if (not= cmd "quit")
        (recur (prompt))))))

(defn -main [& args]
  (hello-msg)
  (process-user-commands))
