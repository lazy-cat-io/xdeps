 (ns xdeps.main
   (:gen-class)
   (:require
     [xdeps.cli :as cli]))


(set! *warn-on-reflection* true)


(defn -main
  "Application entrypoint."
  [& args]
  (cli/dispatch args))
