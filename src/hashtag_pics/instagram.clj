(ns hashtag-pics.instagram
  (:require [clj-http.client :as client]))

(defn api-url [url]
  (str "https://api.instagram.com/v1" url))

(defn fetch [tag client-id]
  (((client/get (api-url (str "/tags/" tag "/media/recent"))
                {:query-params {:client_id client-id}
                 :as :json}) :body) :data))

(defn source [post]
  "instagram")

(defn user [post]
  (select-keys (post :user) [:username :full_name]))

(defn text [post]
  (get-in post [:caption :text]))

(defn pic [post]
  (get-in post [:images :standard_resolution :url]))

(defn extract-info [post]
  (zipmap [:source :user :text :pic] ((juxt source user text pic) post)))

(defn pics [hashtag client-id]
  (map extract-info (fetch hashtag client-id)))
