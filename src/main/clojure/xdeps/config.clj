(ns xdeps.config
  (:refer-clojure :exclude [meta])
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]))


(set! *warn-on-reflection* true)


(defn read-meta
  []
  (some-> "io/lazy-cat/xdeps/meta.edn"
          (io/resource)
          (slurp)
          (edn/read-string)))


(def meta
  (read-meta))
