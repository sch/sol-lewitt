(ns ^:figwheel-always sol-lewitt.core
 (:require [goog.dom :as dom]
           [brutha.core :as react]
           [sol-lewitt.components :as component]
           [sol-lewitt.drawings :as drawings]
           [sol-lewitt.canvas :as canvas]
           [sol-lewitt.routing :as router]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello world!"}))

; (defn on-js-reload (render-app))

(defn get-viewport-dimensions!
  "Return the dimensions of the viewable window as a map with :width and
  :height keys."
  []
  (let [viewport-size (dom/getViewportSize (dom/getWindow))]
    {:width (.-width viewport-size)
     :height (.-height viewport-size)}))

(defn render-app [props]
  (react/mount (component/app props)
               (dom/getElement "app")))

(router/on-change render-app)

