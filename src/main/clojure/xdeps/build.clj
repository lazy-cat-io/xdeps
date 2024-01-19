(ns xdeps.build
  (:refer-clojure :exclude [meta])
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(set! *warn-on-reflection* true)

;; Build metadata

(defn read-meta
  []
  (or (some-> "io/lazy-cat/xdeps/meta.edn"
              (io/resource)
              (slurp)
              (edn/read-string))
      {}))

(def meta
  (read-meta))
