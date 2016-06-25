(ns user
  (:require [mount.core :as mount]
            [emacs-bindings.figwheel :refer [start-fw stop-fw cljs]]
            emacs-bindings.core))

(defn start []
  (mount/start-without #'emacs-bindings.core/repl-server))

(defn stop []
  (mount/stop-except #'emacs-bindings.core/repl-server))

(defn restart []
  (stop)
  (start))
