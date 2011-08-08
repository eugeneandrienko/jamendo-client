;;; Functions in this file are simple wrappers for Jamendo API calls.
;;; For more information about Jamendo API visit:
;;; http://developer.jamendo.com/ru/

(ns jamendo-CLient.jamendoapi-wrappers
  (:require [clj-http.client :as client])
  (:require [clojure.xml :as xml])
  (:import java.io.ByteArrayInputStream)
  (:import java.lang.String)
  (:import java.net.URLEncoder))

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

(defn- get-from-jamendo-smth [field unit params xml-postparser]
  "Get smth content from Jamendo in paged mode."
  (let
      [jamendo-answer (call-jamendo-xml field unit params)
       xml-stream (get-xml-stream jamendo-answer)]
    (xml-postparser (xml/parse xml-stream))))

;; 'keyword' must not have non-Latin Characters (such as
;; Cyrillic, for example - Скрипка) - Jamendo API, called not from
;; browser, just return empty answer.
(defn get-paged-albums
  ([num pagination keyword]
     "Returns the hash-map {id name} of albums which matches with 'keyword'
      from 'pagination' page. All albums divided on 'num' pages."
     (defn album-postparser [xmlstream]
       ;;make {id1 name1, id2 name2} from ((id name) (id name))
       (loop [metalist 
              ;; get <id></id> and <name></name> values
              (for [album-data-level
                    ;; get list of values between <album></album>
                    (for [album-level (if (= (:tag xmlstream) :data)
                                        ;; get data between <data></data>
                                        (:content xmlstream)
                                        nil)
                          :when (= (:tag album-level) :album)]
                      (:content album-level))]
                (list (nth (:content (nth album-data-level 0)) 0)
                      (nth (:content (nth album-data-level 1)) 0)))
              hashmap '{}]
         (if (= metalist nil) hashmap
             (recur
              (next metalist)
              (merge hashmap
                     (hash-map (ffirst metalist)
                               (second (first metalist))))))))
     (get-from-jamendo-smth "id+name" "album"
                            (str "searchquery='"
                                 (URLEncoder/encode keyword) "'"
                                 "&n=" num "&pn=" pagination)
                            (fn [x] (album-postparser x)))))

(defn get-album-songs
  ([album-id]
     "Return hash map {id [name stream]} of songs in album with 'album-id'"
     (defn track-postparser [xmlstream]
       ;;make {id1 name1 stream1, id2 name2 stream2}
       ;;from ((id name stream) (id name stream))
       (loop [metalist 
              ;; get <id></id>, <name></name>, <stream></stream> values
              (for [track-data-level
                    ;; get list of values between <track></track>
                    (for [track-level
                          (if (= (:tag xmlstream) :data)
                            ;;get data between <data></data>
                            (:content xmlstream)
                            nil)
                          :when (= (:tag track-level) :track)]
                      (:content track-level))]
                (list (nth (:content (nth track-data-level 0)) 0)
                      (nth (:content (nth track-data-level 1)) 0)
                      (nth (:content (nth track-data-level 2)) 0)))
              hashmap '{}]
         (if (= metalist nil) hashmap
             (recur
              (next metalist)
              (merge hashmap
                     (hash-map (ffirst metalist)
                               (vector
                                (second (first metalist))
                                (nth (first metalist) 2))))))))
     (get-from-jamendo-smth "id+name+stream" "track"
                            (str "album_id=" album-id)
                            (fn [x] (track-postparser x)))))

(defn get-song [id]
  "Returns stream URL for song with id"
  (get-from-jamendo-smth "stream" "track"
                         (str "id=" id)
                         (fn [x] ((:content ((:content x) 0)) 0))))

;;; API safe functions.
;;; This functions are analog of appropriate get-* functions
;;; which called with 1.1 second delay - this is a requirement
;;; of Jamendo API terms of use.
;;; FIXME: maybe use macros?
(defn get-paged-albums-apisafe
  ([num pagination keyword]
     (. Thread sleep 1100)
     (get-paged-albums num pagination keyword)))
