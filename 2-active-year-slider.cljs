(ns my.app)

; state

(require '[reagent.ratom :as ratom])

(defonce db 
  (ratom/atom {:active-year 1800}))

; state - reactions

(def active-year 
  (ratom/make-reaction 
    (fn [] 
      (@db :active-year))))

; state - transactions

(defn set-active-year! [year]
  (swap! db (fn [prev-db]
              (assoc prev-db :active-year year))))

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

(defn app-view []
  [:div
   [:h1 "Hello World"]
   [slider-view]])

; initialization

(require '[reagent.core :as r])

(defn render []
  (r/render-component [app-view]
    (.. js/document (getElementById "workspace"))))

(render)
