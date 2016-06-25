(ns emacs-bindings.db.core
  (:require
    [conman.core :as conman]
    [mount.core :refer [defstate]]
    [emacs-bindings.config :refer [env]]))

(defstate ^:dynamic *db*
          :start (conman/connect!
                   {:datasource
                    (doto (org.sqlite.SQLiteDataSource.)
                          (.setUrl (env :database-url)))})
          :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")

