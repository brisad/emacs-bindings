(ns emacs-bindings.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [emacs-bindings.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[emacs-bindings started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[emacs-bindings has shut down successfully]=-"))
   :middleware wrap-dev})
