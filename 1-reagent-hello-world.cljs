(ns my.app)

; views

(defn app-view []
  [:div
   [:h1 "Hello World"]])

; initialization

(require '[reagent.core :as r])

(defn render []
  (r/render-component [app-view]
    (.. js/document (getElementById "workspace"))))

(render)

