(ns ^:figwheel-always sol-lewitt.core
    (:require
      [goog.dom :as dom]
      [goog.graphics.SvgGraphics :as svg]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

(defn get-dimensions
  "Return the dimensions of the viewable window"
  []
  (let [viewport-size (dom/getViewportSize (dom/getWindow))]
    [(aget viewport-size "width") (aget viewport-size "height")]))

(println (get-dimensions))

(defn style-elem
  "Style an element"
  [element attrs]
  (let [style-pairs (map #(str (name %1) ":" %2) (keys attrs) (vals attrs))
        style-string (apply str (interpose ";" style-pairs))]
    (dom/setProperties element (.strobj {"style" style-string}))))
