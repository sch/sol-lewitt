(ns lewitt.canvas
 (:require [lewitt.color :as color]))

; More ergonomic to wrap up canvas methods and property setters in clojureish
; functions, and also provide an api that allows us to chain or thread calls by
; returning the context at the end of each method.
(defn begin-path
  [context]
  (.beginPath context)
  context)

(defn move-to
  [context x y]
  (.moveTo context x y)
  context)

(defn line-to
  [context x y]
  (.lineTo context x y)
  context)

(defn end-path
  [context]
  (.endPath context)
  context)

(defn stroke
  [context]
  (.stroke context)
  context)

(defn fill-style
  [context color]
  (set! (.-fillStyle context) color)
  context)

(defn stroke-style
  [context color]
  (set! (.-strokeStyle context) color)
  context)

(defn fill
  [context]
  (.fill context)
  context)

(defn fill-rectangle
  [context x y width height]
  (.fillRect context x y width height)
  context)

(defn draw-rectangle
  [context rectangle]
  (let [color (:color rectangle)
        point (:start-point rectangle)
        dimensions (:dimensions rectangle)]
    (-> context
        (fill-style color)
        (fill-rectangle (:x point)
                        (:y point)
                        (:width dimensions)
                        (:height dimensions)))))

(defn draw-line
  [context line]
  (-> context
      begin-path
      (stroke-style (:color line))
      (move-to (get-in line [:start-point :x])
               (get-in line [:start-point :y]))
      (line-to (get-in line [:end-point :x])
               (get-in line [:end-point :y]))
      stroke))

(defn get-2d-context
  [canvas-element]
  (.getContext canvas-element "2d"))

(defn point [x y] {:x x :y y})

(defn dimensions [width height] {:width width :height height})

(defn rectangle [start-point dimensions color]
  {:type :rectangle
   :color color
   :dimensions dimensions
   :start-point start-point})

(defn demo
  [context canvas-dimensions]
  (-> context
      (draw-rectangle {:color (color/rgb 200 0 0)
                       :start-point (point 15 15)
                       :dimensions (dimensions 50 50)})
      (draw-rectangle (rectangle (point 30 30)
                                 (dimensions 50 50)
                                 (color/rgba 0 0 200 0.5)))
      begin-path
      (move-to 150 150)
      (line-to 175 175)
      (line-to 175 125)
      fill))

