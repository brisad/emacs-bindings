(ns emacs-bindings.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [emacs-bindings.core-test]))

(doo-tests 'emacs-bindings.core-test)

