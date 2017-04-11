(ns ^:figwheel-always lewitt.core
 (:require [goog.dom :as dom]
           [brutha.core :as react]
           [lewitt.components :as component]
           [lewitt.routing :as router]))

(enable-console-print!)

(def on-js-reload (partial println "Reloaded!"))

(defn get-viewport-dimensions!
  "Return the dimensions of the viewable window as a map with :width and
  :height keys."
  []
  (let [viewport-size (dom/getViewportSize (dom/getWindow))]
    {:width (.-width viewport-size)
     :height (.-height viewport-size)}))

(defn render-app [props]
  "The top-level component of the app gets rendered with information coming
  straight out of the URL: a map with :page and :params keys."
  (react/mount (component/app props)
               (dom/getElement "app")))

(router/on-change render-app)

