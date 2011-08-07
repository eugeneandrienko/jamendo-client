(ns jamendo-CLient.core
  (:gen-class)
  (:use clojure.contrib.command-line)
  (:use [jamendo-CLient.user-interface :only [u-search-albums
                                              u-list-album
                                              u-print-album
                                              u-print-song]]))

(defn -main [& args]
  (with-command-line args
    "Jamendo client"
    [[search-albums "Search albums by the arg keyword"]
     [list-album "List album with ID = arg"]
     [print-album "Print stream URLs for songs in album with ID = arg"]
     [print-song "Print stream URL for song with ID = arg"]
     remaining]
    (cond
     search-albums (u-search-albums search-albums)
     list-album (u-list-album list-album)
     print-album (u-print-album print-album)
     print-song (u-print-song print-song)
     :else (println "Unknown argument: " remaining))))
