(ns ^:figwheel-always sol-lewitt.core
    (:require
      [goog.dom :as dom]
      [rum]))

(enable-console-print!)

(println "Starting app...")

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


(rum/defc svg-line [line]
  [:line
   {:x1 (:x (:start-point line))
    :y1 (:y (:start-point line))
    :x2 (:x (:end-point line))
    :y2 (:y (:end-point line))
    :stroke (:color line)
    :stroke-width 1}])

(rum/defc svg-group [contents]
  [:g (map (fn []) contents)])

(rum/defc svg [size drawing]
  [:svg
   {:width (:width size)
    :height (:height size)}
   (map svg-line (:lines drawing))])

(defn size [width height] {:width width :height height})

(defn point [x y] {:x x :y y})

(defn line [start-point end-point] {:start-point start-point
                                    :end-point end-point
                                    :color "green"})

(defn vertical-line [x height]
  (line (point x 0) (point x height)))

(defn horizontal-line [y width]
  (line (point 0 y) (point width y)))

(defn vertical-lines
  [spacing size]
  (map (fn [x] (vertical-line x (:height size)))
       (range 0 (:width size) spacing)))

(defn horizontal-lines
  [spacing size]
  (map (fn [y] (horizontal-line y (:width size)))
       (range 0 (:height size) spacing)))

(def sample-line (line (point 20 20) (point 200 200)))
(def sample-line-2 (vertical-line 30 500))

(def drawing {:lines (concat (vertical-lines 20 (size 3000 200))
                             (horizontal-lines 20 (size 3000 200)))})

(rum/mount (svg (get-dimensions!) drawing) (dom/getElement "app"))

