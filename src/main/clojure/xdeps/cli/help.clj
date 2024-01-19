(ns xdeps.cli.help
  (:require
   [clojure.string :as str]
   [xdeps.build :as build]))

(set! *warn-on-reflection* true)

(defn print-version
  []
  (println (:version build/meta)))

(defn print-meta
  []
  (let [{:keys [version branch commit timestamp]} build/meta
        build (->> [branch commit timestamp]
                   (remove empty?)
                   (str/join ", "))]
    (println (format "xdeps %s (%s)" version build))))

(defn print-usage
  []
  (println "Usage:
  xdeps <command> <options>"))

(defn print-options
  []
  (println "Options:
  --version     Show version
  --help        Show help"))

(defn print-commands
  []
  (println "Commands:")
  (println "TBD"))

(defn print-help
  []
  (print-meta)
  (println)
  (print-usage)
  (println)
  (print-options)
  (println)
  (print-commands))
