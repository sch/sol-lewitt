(ns lewitt.drawings
 (:require [lewitt.canvas :as canvas]
           [lewitt.color :as color]))

(defn point [x y] {:x x :y y})

(defn random-point
  "select a random point within the given bounds"
  [dimensions]
  (point (rand-int (:width dimensions))
         (rand-int (:height dimensions))))

(defn random-angle [] (* (rand) 2 (aget js/Math "PI")))

(defn sin [angle] (.sin js/Math angle))

(defn cos [angle] (.cos js/Math angle))

(defn floor [n] (.floor js/Math n))

(defn line-from
  [start-point angle length]
  {:start-point start-point
   :end-point (point (+ (:x start-point) (floor (* length (cos angle))))
                     (+ (:y start-point) (floor (* length (sin angle)))))})

(defn random-line
  [dimensions length]
  (line-from (random-point dimensions) (random-angle) length))


(defn svg-line [line]
 [:line {:x1 (:x (:start-point line))
         :y1 (:y (:start-point line))
         :x2 (:x (:end-point line))
         :y2 (:y (:end-point line))
         :stroke "gray" #_(:color "teal")
         :stroke-width 1}])

(def drawing-17
  {:id 17
   :title "Wall Drawing 17"
   :instructions "Four-part drawing with a different line direction in each part"
   :url "http://www.massmoca.org/lewitt/walldrawing.php?id=17"})

(def drawing-86
  {:id 86
   :title "Wall Drawing 86"
   :instructions "Ten thousand lines about 10 inches (25 cm) long, covering the wall evenly."
   :date "June 1971"
   :materials "Black pencil"
   :url "http://massmoca.org/event/walldrawing86"
   :algorithm (fn [dimensions] (repeatedly 1000 #(svg-line (random-line dimensions 80))))})

(def drawing-154
  {:id 154
   :title "Wall Drawing 154"
   :instructions "A black outlined square with a red horizontal line from the midpoint of the left side toward the middle of the right side."
   :date "April 1973"
   :materials "Red and black crayon"
   :url "http://massmoca.org/event/walldrawing154"
   :algorithm (fn [dimensions]
                (let [bound (apply min (vals dimensions))]
                  [[:rect {:x 0
                           :y 0
                           :fill "transparent"
                           :stroke "black"
                           :width bound
                           :height bound
                           :stroke-width 3}]
                   [:line {:x1 0
                           :y1 (/ bound 2)
                           :x2 (/ bound 1.5)
                           :y2 (/ bound 2)
                           :stroke "red"
                           :stroke-width 3}]]))})

(def drawing-391
  {:id 391
   :title "Wall Drawing 391"
   :instructions "Two-part drawing. The two walls are each divided horizontally and vertically into four equal..."
   :url "http://massmoca.org/event/walldrawing391"})

(def drawing-999
  {:id 999
   :title "canvas test"
   :type :canvas
   :instructions "Two squares overlapping on a plane"
   :algorithm (fn [dimensions]
                [{:kind :rectangle
                  :color (color/rgb 200 0 0)
                  :start-point {:x 10 :y 10}
                  :dimensions {:width 50 :height 50}}

                 {:kind :rectangle
                  :color (color/rgba 0 0 200 0.5)
                  :start-point {:x 30 :y 30}
                  :dimensions {:width 50 :height 50}}

                 {:kind :rectangle
                  :color (color/rgba 0 0 200 0.5)
                  :start-point {:x 130 :y 130}
                  :dimensions {:width 50 :height 50}}

                 {:kind :line
                  :color (color/rgb 200 200 200)
                  :start-point {:x 30 :y 30}
                  :end-point {:x 130 :y 130}}])
   :renderer :canvas
   :draw canvas/demo})

(def all
  {17  drawing-17
   86  drawing-86
   154 drawing-154
   391 drawing-391
   999 drawing-999})

(defn by-id
  "Return a drawing definition keyed in by its Mass MoCA id number."
  [id]
  (get all id :missing-drawing))
