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

(defn get-image-data
  "Returns the ImageData object from a canvas context"
  [context]
  (js/console.log (.-canvas context))
  (let [element (.-canvas context)
        width (.-width element)
        height (.-height element)]
   (.getImageData context 0 0 width height)))

(defn put-image-data
  [context image-data]
  (.putImageData context image-data 0 0)
  context)

(defn set-pixel
  [image-data [r g b] [x y]]
  (let [data (.-data image-data)
        width (.-width image-data)
        index (* (+ (* y width) x) 4)]
    (aset data index r)
    (aset data (+ index 1) g)
    (aset data (+ index 2) b)
    (aset data (+ index 3) 255)
    image-data))

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

(defn draw-native-line
  [context line]
  (-> context
      begin-path
      (stroke-style (:color line))
      (move-to (get-in line [:start-point :x])
               (get-in line [:start-point :y]))
      (line-to (get-in line [:end-point :x])
               (get-in line [:end-point :y]))
      stroke))

(defn delta
  [x y]
  (.abs js/Math (- x y)))

(defn bresenham-points [x0 y0 x1 y1]
  "Given two points, return a vector of points that lie on the line connecting
  the two according to Bresenham's algorithm"
  {:lifted-from "http://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Clojure"}
  (let [is-steep (> (delta y0 y1) (delta x0 x1))]
    (let [[x0 y0 x1 y1] (if is-steep [y0 x0 y1 x1] [x0 y0 x1 y1])]
      (let [[x0 y0 x1 y1] (if (> x0 x1) [x1 y1 x0 y0] [x0 y0 x1 y1])]
        (let [delta-x (- x1 x0)
              delta-y (js/Math.abs (- y0 y1))
              y-step (if (< y0 y1) 1 -1)]
          (loop [x x0
                 y y0
                 error (js/Math.floor (/ delta-x 2))
                 pixels (if is-steep [[y x]] [[x y]])]
            (if (> x x1)
              pixels
              (if (< error delta-y)
                (recur (inc x)
                       (+ y y-step)
                       (+ error (- delta-x delta-y))
                       (if is-steep (conj pixels [y x]) (conj pixels [x y])))
                (recur (inc x)
                       y
                       (- error delta-y)
                       (if is-steep (conj pixels [y x]) (conj pixels [x y])))))))))))


(defn draw-bresenham-line
  "Draw an aliased line by mutating the image data of a 2DCanvasContext"
  [context line]
  (let [image-data (get-image-data context)
        start-point (:start-point line)
        end-point (:end-point line)
        points (bresenham-points (:x start-point) (:y start-point)
                                 (:x end-point) (:y end-point))]
    (run! (partial set-pixel image-data [0 100 155]) points)
    (put-image-data context image-data)
    context))

(def draw-not-straight-line draw-bresenham-line)

; (def draw-line draw-bresenham-line)
(def draw-line draw-native-line)

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

