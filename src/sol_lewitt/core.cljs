(ns ^:figwheel-always sol-lewitt.core
 (:require [goog.dom :as dom]
           [brutha.core :as react]
           [sol-lewitt.components :as component]
           [sol-lewitt.drawings :as drawings]
           [sol-lewitt.canvas :as canvas]
           [sol-lewitt.routing :as routing]))

(defn log [item] (.log js/console (pr-str item)))

(enable-console-print!)

#_(println "Starting app...")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn on-js-reload [])
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)

(defn get-viewport-dimensions!
  "Return the dimensions of the viewable window as a map with :width and
  :height keys."
  []
  (let [viewport-size (dom/getViewportSize (dom/getWindow))]
    {:width (.-width viewport-size)
     :height (.-height viewport-size)}))

(defn render-app [props]
  (react/mount (component/canvas-squares (get-viewport-dimensions!)
                                         (drawings/by-id (:id props)))
               (dom/getElement "app")))

(render-app {:id 86})
