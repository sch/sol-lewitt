(ns ^:figwheel-always sol-lewitt.core
    (:require
      [goog.dom :as dom]
      [goog.graphics :as graphics]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

(defn get-dimensions!
  "Return the dimensions of the viewable window"
  []
  (let [viewport-size (dom/getViewportSize (dom/getWindow))]
       {:width (aget viewport-size "width")
        :height (aget viewport-size "height")}))

(println (get-dimensions!))


(defn draw-svg!
  "Instantiate an empty SvgGraphics object with the given dimensions map"
  [dimensions]
  (let [w (:width dimensions)
        h (:height dimensions)
        svg-graphics (new graphics/SvgGraphics w h)]
       (.createDom svg-graphics)
       svg-graphics))

(defn render
  [svg id]
  (dom/appendChild (dom/getElement id) (.getElement svg)))

(defn line
  [x1 y1 x2 y2]
  (.lineTo (.moveTo (new graphics/Path) x1 y1) x2 y2))


(defn stroke
  "build a dumb google stroke object"
  []
  (new graphics/Stroke 1 "#333"))

(defn add-line
  "Add a line to an svg object"
  [svg x1 y1 x2 y2]
  (.drawPath svg (line x1 y1 x2 y2) (stroke))
  svg)

(defn add-vertical-line
  [svg x]
  (add-line svg x 0 x (aget (.getPixelSize svg) "height"))
  svg)

(defn add-vertical-lines
  "Make a bunch of vertical lines spaced apart "
  [svg spacing]
  (let [extent (aget (.getPixelSize svg) "width")]
       (reduce (fn [svg x] (add-vertical-line svg x))
               svg
               (range 0 extent spacing))))


(defn new-canvas []
  (draw-svg! (get-dimensions!)))

(let [canvas (draw-svg! (get-dimensions!))]
     (render (-> canvas
                 (add-vertical-lines 30)
                 (add-vertical-lines 50)) "app"))

(defn style-elem
  "Style an element"
  [element attrs]
  (let [style-pairs (map #(str (name %1) ":" %2) (keys attrs) (vals attrs))
        style-string (apply str (interpose ";" style-pairs))]
    (dom/setProperties element (.strobj {"style" style-string}))))
