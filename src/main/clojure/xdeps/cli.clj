(ns xdeps.cli
  (:require
    [babashka.cli :as cli]
    [xdeps.cli.help :as help]))


(set! *warn-on-reflection* true)


(def default
  {:cmds []
   :spec {:version {:coerce :boolean}}
   :fn (fn [{:keys [opts]}]
         (if (:version opts)
           (help/print-version)
           (help/print-help)))})


(def cmds
  [default])


(defn dispatch
  [args]
  (cli/dispatch cmds args {}))
