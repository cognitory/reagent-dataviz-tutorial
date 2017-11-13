(ns my.app)

; state

(require '[reagent.ratom :as ratom])

(defonce db 
  (ratom/atom {:data []
               :active-year 1800}))

; state - reactions

(def active-year 
  (ratom/make-reaction 
    (fn [] 
      (@db :active-year))))

(def data
  (ratom/make-reaction 
    (fn [] 
      (@db :data))))

(def active-year-data
  (ratom/make-reaction
    (fn []
      (let [year @active-year]
        (->> @data
             (filter (fn [datum] (= year (datum :year)))))))))

; state - transactions

(defn set-active-year! [year]
  (swap! db (fn [prev-db]
              (assoc prev-db :active-year year))))

(defn set-data! [data]
  (swap! db assoc :data data))

; views

(defn graph-view []
  (let [interpolate (fn 
                      [x [x0 x1] [y0 y1]]
                      (+ y0 (/ (* (- x x0)
                                  (- y1 y0))
                               (- x1 x0))))
        w 400
        h 200
        p 20]

    [:svg {:width (+ p p w) 
           :height (+ p p h) 
           :style {:margin "1em"}}

     [:g {:transform (str "translate(" p "," p ")")}

      [:g {:class "x-axis"}
       [:line {:x1 0 :y1 h
               :x2 w :y2 h
               :stroke "black"
               :stroke-width 1}]
       [:text {:x (/ w 2) :y (+ h 15) 
               :text-anchor "middle"} 
        "GDP"]]

      [:g {:class "y-axis"}
       [:line {:x1 0 :y1 h
               :x2 0 :y2 0
               :stroke "black"
               :stroke-width 1}]
       [:text {:x 0 :y (/ h 2) 
               :text-anchor "middle" 
               :transform "rotate(-90 0 100)"} 
        "Life Expectancy"]]

      (let [x-range [0 (apply max (map :gdp @data))]
            y-range [0 (apply max (map :life-expectancy @data))]
            z-range [0 (apply max (map :population @data))]
            ->x (fn [v]
                  (interpolate v x-range [0 w]))
            ->y (fn [v]
                  (interpolate v (reverse y-range) [0 h]))
            ->z (fn [v]
                  (interpolate v z-range [2 10]))]
        (doall
          (for [datum @active-year-data]
            [:circle {:key (datum :country)
                      :cx (->x (datum :gdp)) 
                      :cy (->y (datum :life-expectancy)) 
                      :r (->z (datum :population))
                      :style {:transition "cx 1s ease-in-out, 
                                          cy 1s ease-in-out, 
                                          r 1s ease-in-out"}}])))]]))

(defn slider-view []
  [:div
   [:input {:type "range"
            :min 1800
            :max 2000
            :step 50
            :value @active-year
            :on-change (fn [e]
                         (set-active-year! (js/parseInt (.. e -target -value) 10)))}]
   @active-year])

(defn table-view []
  [:table
   [:thead
    [:tr
     [:th "Year"]
     [:th "Country"]
     [:th "Life Expectancy"]
     [:th "GDP"]
     [:th "Population"]]]
   [:tbody
    (doall
      (for [datum @active-year-data]
        [:tr {:key (str (datum :country) (datum :year))}
         [:td (datum :year)]
         [:td (datum :country)] 
         [:td (datum :life-expectancy)]
         [:td (datum :gdp)]
         [:td (datum :population)]]))]])

(defn app-view []
  [:div
   [:h1 "Hello World"]
   [slider-view]
   [table-view]])

; initialization

(require '[reagent.core :as r])

(defn render []
  (r/render-component [app-view]
    (.. js/document (getElementById "workspace"))))

(render)

(require '[cljs.pprint :as p])
(require '[tutorial.helpers :as h])

(h/get-data (fn [data]
              (p/pprint data)
              (set-data! data)))
