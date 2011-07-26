(ns jamendo-CLient.core
  (:gen-class)
  (:use [jamendo-CLient.jamendoapi-wrappers :only [get-paged-tags]]
        [jamendo-CLient.user-interface :only [print-paged-list]]))

(defn -main [& args]
  (print-paged-list
   (fn [x] (get-paged-tags 10 x))))
