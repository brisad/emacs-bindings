(ns emacs-bindings.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [emacs-bindings.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST PUT DELETE]])
  (:import goog.History))

(def bindings (r/atom []))

(defn get-bindings []
  (GET "/api/bindings" {:handler #(reset! bindings %)}))

(get-bindings)

(defn delete-binding [id]
  (DELETE (str "/api/binding/" id)
          {:handler #(get-bindings)}))

(defn increase-usage [id]
  (PUT (str "/api/binding/" id "/use")
       {:handler #(get-bindings)}))

(defn bindings-list [bindings]
  [:table.table.table-bordered
   [:thead
    [:tr
     [:th "#"]
     [:th "Key"]
     [:th "Command"]
     [:th "Description"]
     [:th {:col-span 2} "Times Used"]
     [:th]]]
   [:tbody
    (for [{:keys [id binding command description times_used]} bindings]
      ^{:key id}
      [:tr
       [:td id]
       [:td binding]
       [:td command]
       [:td description]
       [:td [:button.btn.btn-default
             {:type :button
              :on-click
              #(increase-usage id)}
             "+1"]]
       [:td times_used]
       [:td [:button.btn.btn-link.btn-sm
             {:type :button
              :on-click
              #(delete-binding id)}
             "Delete"]]])]])

(defn input [form tag label field]
  [:div.form-group
   [:label label]
   [tag
    {:type :text
     :value (field @form)
     :on-change
     #(swap! form
             assoc
             field (-> % .-target .-value))}]])

(defn add-binding [form]
  (POST "/api/add_binding"
        {:params @form
         :handler
         #(get-bindings)}))


(defn add-binding-form []
  (let [form (r/atom {})]
    (fn []
      [:div
       [:h2 "New binding"]
       [:form
        [input form :input.form-control "Key" :binding]
        [input form :input.form-control "Command" :command]
        [input form :textarea.form-control "Description" :description]
        [:button.btn.btn-primary
         {:on-click #(do
                       (add-binding form)
                       (reset! form {}))} "Add binding"]]])))

(defn home-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:h2 "Emacs key bindings"]
     [bindings-list @bindings]
     [add-binding-form]]]])

(def pages
  {:home #'home-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
