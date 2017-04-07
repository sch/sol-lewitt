(ns lewitt.server
 (:require [clojure.string :refer [starts-with? includes?]]
           [ring.middleware.resource :refer [wrap-resource]]))

(defn- wrap-default-index [next-handler]
  "Forward all non-static resources to index.html"
  (fn [request]
    (next-handler
     (if (or (starts-with? (:uri request) "/css/")
             (starts-with? (:uri request) "/js/"))
       request
       (assoc request :uri "/index.html")))))

(def handler
  (-> (fn [_] {:status 404 :body "static asset not found"})
     (wrap-resource "public")
     wrap-default-index))
