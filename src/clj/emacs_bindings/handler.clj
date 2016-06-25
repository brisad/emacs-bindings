(ns emacs-bindings.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [emacs-bindings.layout :refer [error-page]]
            [emacs-bindings.routes.home :refer [home-routes]]
            [emacs-bindings.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [emacs-bindings.env :refer [defaults]]
            [mount.core :as mount]
            [emacs-bindings.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    #'service-routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
