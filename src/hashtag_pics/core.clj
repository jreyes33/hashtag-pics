(ns hashtag-pics.core
  (:require [clojure.data.json :as json]
            [hashtag-pics.instagram :as instagram]
            [hashtag-pics.twitter :as twitter])
  (:gen-class))

(defn -main [hashtag instagram-client-id twitter-consumer-key twitter-consumer-secret]
  (json/pprint-json (instagram/pics hashtag instagram-client-id))
  (json/pprint-json (twitter/pics hashtag twitter-consumer-key twitter-consumer-secret)))
