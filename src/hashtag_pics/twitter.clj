(ns hashtag-pics.twitter
  (:require [clj-http.client :as client]
            [clojure.data.codec.base64 :as base64]))

(defn token [consumer-key consumer-secret]
  (String. (base64/encode (.getBytes (str consumer-key ":" consumer-secret)))
           "UTF-8"))

(defn api-url [url]
  (str "https://api.twitter.com" url))

(defn auth [consumer-key consumer-secret]
  (((client/post (api-url "/oauth2/token")
                 {:headers {"Authorization" (str "Basic " (token consumer-key consumer-secret))
                            "Content-Type" "application/x-www-form-urlencoded;charset=UTF-8"}
                  :body "grant_type=client_credentials"
                  :as :json}) :body) :access_token))

(defn fetch [hashtag consumer-key consumer-secret]
  (let [access-token (auth consumer-key consumer-secret)]
    (((client/get (api-url "/1.1/search/tweets.json")
                  {:headers {"Authorization" (str "Bearer " access-token)}
                   :query-params {:count 100
                                  :result_type "recent"
                                  :q (str "filter:images #" hashtag)}
                   :as :json}) :body) :statuses)))

(defn source [post]
  "twitter")

(defn user [post]
  (clojure.set/rename-keys (select-keys (post :user) [:screen_name :name])
                           {:name :full_name :screen_name :username}))

(defn text [post]
  (post :text))

(defn pic [post]
  (str ((first (get-in post [:entities :media])) :media_url) ":large"))

(defn extract-info [post]
  (zipmap [:source :user :text :pic] ((juxt source user text pic) post)))

(defn pics [hashtag consumer-key consumer-secret]
  (map extract-info (fetch hashtag consumer-key consumer-secret)))
