(defproject hashtag-pics "0.1.0-SNAPSHOT"
  :description "A simple app that grabs pictures from social media sites based on hashtags"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-http "1.0.0"]]
  :main ^:skip-aot hashtag-pics.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
