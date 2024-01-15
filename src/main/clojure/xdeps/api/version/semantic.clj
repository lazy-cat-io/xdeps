(ns xdeps.api.version.semantic
  (:require
    [clojure.string :as str]
    [malli.core :as m])
  (:import
    (java.io
      Writer)))


(set! *warn-on-reflection* true)


(def Version
  [:map
   [:major [:int {:min 0}]]
   [:minor [:int {:min 0}]]
   [:patch [:int {:min 0}]]
   [:pre-release [:maybe string?]]
   [:build [:maybe string?]]])


(def re
  (->> ["^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)"
        "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)"
        "(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?"
        "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$"]
       (str/join)
       (re-pattern)))


(defprotocol ISemanticVersion
  (valid? [x] "Returns `true` if 'x` satisfies the semantic version.")
  (parse [x] "Returns an instance of `xdeps.api.version.semantic/SemanticVersion` if `x` satisfies the semantic version. Otherwise, returns `nil`.")
  (bump [x part] "Returns an instance of `xdeps.api.version.semantic/SemanticVersion` with the incremented part (`:major`, `:minor` or `:patch`)."))


(defrecord SemanticVersion
  [major minor patch pre-release build]
  ISemanticVersion
  (valid? [v] (m/validate Version v))
  (parse [v] v)
  (bump [v part]
    (case part
      :major (-> v (update :major inc) (assoc :minor 0 :patch 0))
      :minor (-> v (update :minor inc) (assoc :patch 0))
      :patch (update v :patch inc)
      (throw (ex-info (format "Can't bump %s version" part) {:given part, :allowed #{:major :minor :patch}}))))

  Object
  (toString [_]
    (cond-> (format "%s.%s.%s" major minor patch)
      pre-release (str "-" pre-release)
      build (str "+" build))))


(defmethod print-method SemanticVersion
  [v ^Writer w]
  (print-method (into {} v) w))


(defmethod print-dup SemanticVersion
  [v ^Writer w]
  (print-method v w))


(extend-protocol ISemanticVersion
  nil
  (valid? [_] false)
  (parse [_] nil)
  (bump [_ _] nil)

  String
  (valid? [s] (->> s (re-matches re) (boolean)))
  (parse [s]
    (when (valid? s)
      (let [[[_s major minor patch pre-release build]] (re-seq re s)]
        (map->SemanticVersion
          {:major (parse-long major)
           :minor (parse-long minor)
           :patch (parse-long patch)
           :pre-release pre-release
           :build build}))))
  (bump [s part] (-> s (parse) (bump part))))
