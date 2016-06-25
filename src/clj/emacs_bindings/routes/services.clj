(ns emacs-bindings.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [emacs-bindings.db.core :as db]))

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Emacs bindings API"
                           :description ""}}}}
  (context "/api" []
    :tags ["bindings"]

    (GET "/bindings" []
         :return       [{:id Integer
                         :binding String
                         :command String
                         :description String
                         :times_used Integer}]
      :query-params []
      :summary      "List all bindings"
      (ok (db/all-bindings)))

    (POST "/add_binding" []
      :return      Long
      :body-params [binding :- String,
                    command :- String,
                    description :- String]
      :summary     "Add new binding"
      (ok (db/add-binding {:binding binding
                           :command command
                           :description description})))

    (DELETE "/binding/:id" []
            :return      Long
            :path-params [id :- Long]
            :summary     "Delete binding"
            (ok (db/delete-binding {:id id})))

    (PUT "/binding/:id/use" []
         :return      Long
         :path-params [id :- Long]
         :summary     "+1 a binding"
         (ok (db/increase-usage {:id id})))))
