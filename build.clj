(ns build
  (:require
   [clojure.string :as str]
   [clojure.tools.build.api :as b]))

(def lib 'io.lazy-cat/xdeps)
(def version (-> "version" (slurp) (str/trim-newline)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file "target/xdeps.jar")
(def src-dirs ["src/main/clojure" "src/main/resources"])
(def main 'xdeps.main)

(defn uberjar
  [_]
  (println "Writing pom.xml...")
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs src-dirs})
  (println "Copying sources...")
  (b/copy-dir {:src-dirs src-dirs
               :target-dir class-dir})
  (println "Compiling sources...")
  (b/compile-clj {:basis basis
                  :src-dirs src-dirs
                  :class-dir class-dir})
  (println "Building uberjar...")
  (b/uber {:class-dir class-dir
           :uber-file jar-file
           :basis basis
           :main main})
  (println "Done..."))
