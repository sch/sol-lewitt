(ns lewitt.components
 (:require [brutha.core :as react]
           [sablono.core :as html :refer-macros [html]]
           [cljsjs.react-virtualized]
           [lewitt.drawings :as drawings]
           [lewitt.routing :refer [link-to link-to-drawing]]))

(defn with-size
  "A React component that will render a child with the parent element's dimensions
  passed in a map to the function"
  [fun]
  (.createElement js/React
                  js/ReactVirtualized.AutoSizer
                  #js {}
                  #(fun {:width (.floor js/Math (.-width %))
                         :height (.floor js/Math (.-height %))})))

(defn drawing-information [drawing]
  [:div.Work
   [:div.Work-title (:title drawing)]
   [:div.Work-algorithm (:instructions drawing)]
   [:div.Work-meta
    [:a {:href (:url drawing)} "Mass MoCA Piece"]
    [:div [:a {:href (link-to :home)} "back"]]]])


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
  (let [padding 0]
   [:svg
    {:width (- (:width size) (* 2 padding))
     :height (- (:height size) (* 2 padding))
     :style [:padding padding]}
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

(defn index
  [drawings]
  [:div.Home
   [:h1 "Sol Lewitt Wall Drawings"]
   [:ul.Home-list
    (map (fn [[_ drawing]] [:li.Home-drawing
                            [:a {:href (link-to-drawing (:id drawing))} (:title drawing)]
                            [:div (:instructions drawing)]])
         drawings)]])

; ctx.beginPath();
; ctx.moveTo(75, 50);
; ctx.lineTo(100, 75);
; ctx.lineTo(100, 25);
; ctx.fill()]]);

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

(defn fill
  [context]
  (.fill context)
  context)

(defn fill-rectangle
  [context x y width height]
  (.fillRect context x y width height)
  context)

(defn draw-line
 [context start-x start-y end-x end-y]
 (-> context
     begin-path
     (move-to start-x start-y)
     (line-to end-x end-y)
     end-path))

(defn get-2d-context
 [canvas-element]
 (.getContext canvas-element "2d"))

(defn rgb [r g b] (str "rgb(" r ", " g ", " b ")"))

(defn rgba [r g b a] (str "rgba(" r ", " g ", " b ", " a ")"))

(def canvas-squares
 "A component that renders squares on a canvas"
 (react/component
   (reify
     react/IDidMount
     (did-mount
       [_ value dom-node]
       (-> (get-2d-context dom-node)
           (fill-style (rgb 200 0 0))
           (fill-rectangle 10 10 50 50)
           (fill-style (rgba 0 0 200 0.5))
           (fill-rectangle 30 30 50 50)
           begin-path
           (move-to 150 150)
           (line-to 175 175)
           (line-to 175 125)
           fill))

     react/IRender
     (render
       [_ props]
       (html [:canvas {:width (:width props)
                       :height (:height props)}])))))

(defn project
 [drawing]
 [:div.Piece [:div.Piece-canvas (with-size (fn [size] (if (= (:type drawing) :canvas)
                                                          (canvas-squares size)
                                                          (html (svg size (:algorithm drawing))))))]
             (drawing-information drawing)])

(defn app
  [props]
  (html (case (:page props)
              :home (index drawings/all)
              :drawing (project (drawings/by-id (-> props :route-params :id int))))))
