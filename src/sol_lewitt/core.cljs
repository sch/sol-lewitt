(ns ^:figwheel-always sol-lewitt.core
    (:require
      [goog.dom :as dom]
      [rum]))

(defn log [item] (.log js/console (pr-str item)))

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

(rum/defc drawing-information [project]
  [:div
   {:class "Work"}
   [:div {:class "Work-title"} (:title project)]
   [:div {:class "Work-algorithm"} (:instructions project)]
   [:div
    {:class "Work-meta"}
    [:a {:href (:url project)} "Mass MoCA Piece"]]])


(defn svg-line [line]
  [:line {:x1 (:x (:start-point line))
          :y1 (:y (:start-point line))
          :x2 (:x (:end-point line))
          :y2 (:y (:end-point line))
          :stroke (:color line)
          :stroke-width 1}])

(defn svg-translation-string
  "String used for the translation attribute of an svg element"
  [point]
  (str "translate(" (:x point) ", " (:y point) ")"))

(defn svg-group
  ([contents] [:g nil contents])
  ([contents offset] [:g {:transform (svg-translation-string offset)} contents]))

(rum/defc svg [size drawing]
  [:svg
   {:width (:width size)
    :height (:height size)}
   drawing])

(defn size [width height] {:width width :height height})

(defn point [x y] {:x x :y y})

(defn line [start-point end-point]
  {:start-point start-point
   :end-point end-point
   :color "green"})

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


;; Some Definitions to play around with ::

(def sample-line (line (point 20 20) (point 200 200)))
(def sample-line-2 (vertical-line 30 500))

(def verticals (vertical-lines 20 (size 200 200)))
(def horizontals (horizontal-lines 20 (size 200 200)))

(def drawing-17-information
  {:title "Wall Drawing 17"
   :instructions "Four-part drawing with a different line direction in each part"
   :url "http://www.massmoca.org/lewitt/walldrawing.php?id=17"})

(def drawing-17
  [:g
   nil
   (svg-group (map svg-line verticals) {:x 300 :y 50})
   (svg-group (map svg-line horizontals) {:x 50 :y 50})])

(rum/defc project
  [dimensions drawing information]
  [:div nil (svg dimensions drawing) (drawing-information information)])

(rum/mount (project (get-dimensions!)
                    drawing-17
                    drawing-17-information)
           (dom/getElement "app"))
