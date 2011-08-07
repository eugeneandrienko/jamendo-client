(ns jamendo-CLient.player
  (:require [clojure.contrib.shell-out :as shout]))

(defn play-stream [surl]
  (shout/sh "mplayer" " " surl))
