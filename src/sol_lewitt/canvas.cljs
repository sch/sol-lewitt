(ns sol-lewitt.canvas)

(defn draw-rectangle
  [context point]
  (do (.fillStyle context (:color point))
      (.fillRect context (:x point) (:y point) (:width point) (:height point))
      context))

(defn draw
  [context dimensions points]
  (.save context)
  (.clearRect 0 0 (:width dimensions) (:height dimensions))
  (run! (partial draw-rectangle context) points))
