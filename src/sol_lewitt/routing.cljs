(ns sol-lewitt.routing
 (:require [bidi.bidi :as router]
           [core.set :refer [rename-keys]]))

;; This file sets up general-purpose routing for the application. We use the
;; Bidi router to accomplish url parsing and url generation based on a
;; definition.

(def routes
  ["/" {"" :home
        "about" :about
        ["drawings/" [#"\d+" :id]] :drawing}]) ; Match routes like /drawings/51

(defn parse [url]
  (rename-keys (router/match-route routes url)
               {:handler :path}))

(defn link-to [handler] (router/path-for routes handler))

(defn link-to-drawing [number] (router/path-for routes :id number))
