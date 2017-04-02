(ns ^:figwheel-always sol-lewitt.core
 (:require
   [goog.dom :as dom]
   [rum.core :as rum]
   [sol-lewitt.drawings :as drawings]
   [sol-lewitt.canvas :as canvas]))

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
  "Return the dimensions of the viewable window"
  []
  (let [viewport-size (dom/getViewportSize (dom/getWindow))]
    {:width (aget viewport-size "width")
     :height (aget viewport-size "height")}))

(defn drawing-information [drawing]
  [:div.Work
   [:div.Work-title (:title drawing)]
   [:div.Work-algorithm (:instructions drawing)]
   [:div.Work-meta
    [:a {:href (:url drawing)} "Mass MoCA Piece"]]])


(defn svg-line [line]
  [:line {:x1 (:x (:start-point line))
          :y1 (:y (:start-point line))
          :x2 (:x (:end-point line))
          :y2 (:y (:end-point line))
          :stroke "gray" #_(:color "teal")
          :stroke-width 1}])

(defn svg-translation-string
  "String used for the translation attribute of an svg element"
  [{:keys [x, y]}]
  (str "translate(" x ", " y ")"))

(defn svg-group
  ([contents] [:g contents])
  ([contents offset] [:g {:transform (svg-translation-string offset)} contents]))



(defn svg [size generate-drawing]
  (let [padding 50]
   [:svg
    {:width (- (:width size) (* 2 padding))
     :height (- (:height size) (* 2 padding))
     :padding padding}
    (generate-drawing size)]))

(defn size [width height] {:width width :height height})

(defn point [x y] {:x x :y y})

(defn line [start-point end-point]
  {:start-point start-point
   :end-point end-point
   :color "aquamarine"})

(defn vertical-line [x height]
  (line (point x 0) (point x height)))

(defn horizontal-line [y width]
  (line (point 0 y) (point width y)))

(defn vertical-lines
  [spacing size]
  (map (fn [x] (vertical-line x (:height size)))
       (range 0 (inc (:width size)) spacing)))

(defn horizontal-lines
  [spacing size]
  (map (fn [y] (horizontal-line y (:width size)))
       (range 0 (inc (:height size)) spacing)))

(def sample-line (line (point 20 20) (point 200 200)))
(def sample-line-2 (vertical-line 30 500))

(def verticals (vertical-lines 20 (size 200 200)))
(def horizontals (horizontal-lines 20 (size 200 200)))

(defn arbitrary-html
  ([html] (arbitrary-html nil html))
  ([props html] [:div (merge {:__dangerouslySetInnerHTML {:__html html}} props)]))

(def drawing-17
  [:g
   (svg-group (map svg-line verticals)   {:x 300 :y 50})
   (svg-group (map svg-line horizontals) {:x 50 :y 50})
   (svg-group (map svg-line verticals)   {:x 50 :y 300})
   (svg-group (map svg-line horizontals) {:x 300 :y 300})
   (svg-group (map svg-line horizontals) {:x 650 :y 50})])

(rum/defc project
  [dimensions drawing]
  [:div (svg dimensions (:algorithm drawing))
        (drawing-information drawing)])

(rum/defc canvas-wrapper < {:did-mount #(log "started rendering canvas")}
  "This component wraps a canvas element in an interface that calls setup and
   draw commands to "
  []
  [:canvas])

(rum/mount (project (get-viewport-dimensions!) drawings/drawing-86)
           (dom/getElement "app"))
