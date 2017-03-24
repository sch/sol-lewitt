(ns sol-lewitt.drawings)

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

(def drawing-17
  {:title "Wall Drawing 17"
   :instructions "Four-part drawing with a different line direction in each part"
   :url "http://www.massmoca.org/lewitt/walldrawing.php?id=17"})

(def drawing-86
  {:title "Wall Drawing 86"
   :instructions "Ten thousand lines about 10 inches (25 cm) long, covering the wall evenly."
   :date "June 1971"
   :materials "Black pencil"
   :url "http://massmoca.org/event/walldrawing86"
   :algorithm (fn [dimensions] (repeatedly 1000 #(random-line dimensions 80)))})

(def drawing-154
  {:title "Wall Drawing 154"
   :instructions "A black outlined square with a red horizontal line from the midpoint of the left side toward the middle of the right side."
   :date "April 1973"
   :materials "Red and black crayon"
   :url "http://massmoca.org/event/walldrawing154"
   :algorithm (fn [dimensions]
                [[:rect (merge {:x 0 :y 0 } dimensions)]])})
