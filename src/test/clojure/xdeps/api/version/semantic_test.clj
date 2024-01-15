(ns xdeps.api.version.semantic-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [xdeps.api.version.semantic :as sut]))


(deftest parse-test
  (testing "correct conversion to a string"
    (doseq [v ["0.1.0"
               "0.1.0-SNAPSHOT"
               "0.1.0+master-bb1023"
               "0.1.0-alpha+master-bb1023"]]
      (is (= v (str (sut/parse v)))))))


(deftest bump-test
  (testing "correct version bump"
    (testing "major version"
      (let [v (sut/bump "0.1.2" :major)]
        (is (= "1.0.0" (str v)))
        (is (= {:major 1, :minor 0, :patch 0, :pre-release nil, :build nil}
               (into {} v)))))

    (testing "minor version"
      (let [v (sut/bump "0.1.2" :minor)]
        (is (= "0.2.0" (str v)))
        (is (= {:major 0, :minor 2, :patch 0, :pre-release nil, :build nil}
               (into {} v)))))

    (testing "patch version"
      (let [v (sut/bump "0.1.2" :patch)]
        (is (= "0.1.3" (str v)))
        (is (= {:major 0, :minor 1, :patch 3, :pre-release nil, :build nil}
               (into {} v)))))))
