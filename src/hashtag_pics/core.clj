(ns hashtag-pics.core
  (:require [clj-http.client :as client]
            [clojure.data.codec.base64 :as base64]
            [clojure.data.json :as json])
  (:gen-class))

(defn token [consumer-key consumer-secret]
  (String. (base64/encode (.getBytes (str consumer-key ":" consumer-secret)))
           "UTF-8"))

(defn twitter-url [url]
  (str "https://api.twitter.com" url))

(defn auth [consumer-key consumer-secret]
  (((client/post (twitter-url "/oauth2/token")
                 {:headers {"Authorization" (str "Basic " (token consumer-key consumer-secret))
                            "Content-Type" "application/x-www-form-urlencoded;charset=UTF-8"}
                  :body "grant_type=client_credentials"
                  :as :json}) :body) :access_token))

(defn find-tweets [query consumer-key consumer-secret]
  (let [access-token (auth consumer-key consumer-secret)]
    (((client/get (twitter-url "/1.1/search/tweets.json")
                  {:headers {"Authorization" (str "Bearer " access-token)}
                   :query-params {:count 100
                                  :result_type "recent"
                                  :q (str query " filter:images")}
                   :as :json}) :body) :statuses)))

(defn pic [tweet]
  (str ((first (get-in tweet [:entities :media])) :media_url)
       ":large"))

(defn user [tweet]
  (select-keys (tweet :user) [:name :screen_name]))

(defn text [tweet]
  (tweet :text))

(defn extract-info [tweet]
  (zipmap [:user :text :pic] ((juxt user text pic) tweet)))

(defn -main [query consumer-key consumer-secret]
  (json/pprint-json (map extract-info (find-tweets query consumer-key consumer-secret))))
