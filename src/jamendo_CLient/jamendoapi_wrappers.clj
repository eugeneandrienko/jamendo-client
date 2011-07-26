;;; Functions in this file are simple wrappers for Jamendo API calls.
;;; For more information about Jamendo API visit:
;;; http://developer.jamendo.com/ru/

(ns jamendo-CLient.jamendoapi-wrappers
  (:require [clj-http.client :as client])
  (:require [clojure.xml :as xml])
  (:import java.io.ByteArrayInputStream)
  (:import java.lang.String))

(defn- call-jamendo-xml [field unit params]
  (client/get
   (str "http://api.jamendo.com/get2/"
        field
        "/"
        unit
        "/xml/?"
        params)))

(defn- get-xml-stream [jamendo-answer]
  "Returns xml from answer of Jamendo API in ByteArrayInputStream object"
  (ByteArrayInputStream.
   (.getBytes
    (cond
     (= (:status jamendo-answer) 200) (:body jamendo-answer)
     :else nil)
    "UTF-8")))

(defn get-paged-tags [num pagination]
  "Returns the list of tags from 'pagination' page. All tags
   divided on 'num' pages."
  (let
      [jamendo-answer (call-jamendo-xml "name" "tag"
                                        (str "n=" num "&pn=" pagination))
       xml-stream (get-xml-stream jamendo-answer)]
    (map
     (fn [x] (:content x))
     (:content (xml/parse xml-stream)))))

;; func - should be lambda function with one parameter - number
;; of requested page, which calls proper function with necessary
;; parameters.
(defn- get-list-with-delay [func]
  "Makes list from lists returned by 'get-paged-*' functions with
   one second delay between calls."
  (defn- get-list-with-delay-iter [func num]
    ; we need to sleep one second or more  before each call - this is a
    ; requirement of Jamendo API Terms Of Use.
    (. Thread sleep 1500)
    (let [requested-list (func num)]
      (println requested-list)
      (cond
       (= requested-list '()) '()
       :else (concat requested-list
                     (get-list-with-delay-iter func (+ num 1))))))
  (get-list-with-delay-iter func 1))
