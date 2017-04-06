(ns sol-lewitt.routing
 (:require [bidi.bidi :as router]
   [clojure.set :refer [rename-keys]]
   [pushy.core :as pushy]))

;; This file sets up general-purpose routing for the application. We use the
;; Bidi router to accomplish url parsing and url generation based on a
;; definition.

(def routes
  ["/" {"" :home
        "about" :about
        ["drawings/" [#"\d+" :id]] :drawing}]) ; Match routes like /drawings/51

(defn parse-url [url]
  (rename-keys (router/match-route routes url)
               {:handler :page}))

(defn on-change
  [dispatch-fn]
  (pushy/start! (pushy/pushy dispatch-fn parse-url)))

(defn link-to [handler] (router/path-for routes handler))

(defn link-to-drawing [number] (router/path-for routes :drawing :id number))
