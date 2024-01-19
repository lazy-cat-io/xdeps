(ns xdeps.config
  (:require
   [babashka.fs :as fs]
   [clojure.edn :as edn]))

(set! *warn-on-reflection* true)

;; Default xdeps configuration

(def defaults
  {})

(defn read-config
  [root]
  (let [path (fs/path root "config.edn")]
    (if (fs/exists? path)
      (some-> path (str) (slurp) (edn/read-string))
      (do
        ;; provide the default config
        (fs/create-dir root)
        (spit (str path) defaults)
        defaults))))

;; Global xdeps configuration

#_:clj-kondo/ignore

(def global
  (-> "xdeps"
      (fs/xdg-config-home)
      (read-config)))
