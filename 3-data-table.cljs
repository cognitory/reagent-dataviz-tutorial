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

; state - transactions

(defn set-active-year! [year]
  (swap! db (fn [prev-db]
              (assoc prev-db :active-year year))))

(defn set-data! [data]
  (swap! db assoc :data data))

; views

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
      (for [datum @data]
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
