(ns xdeps.api.version.semantic-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [xdeps.api.version.semantic :as sut]))

(deftest ^:unit parse-test
  (testing "correct conversion to a string"
    (doseq [v ["0.1.0"
               "0.1.0-SNAPSHOT"
               "0.1.0+master-bb1023"
               "0.1.0-alpha+master-bb1023"]]
      (is (= v (str (sut/parse v)))))))

(deftest ^:unit bump-test
  (testing "correct version bump"

    (testing "string -> string"
      (testing "major version"
        (is (= "1.0.0" (sut/bump "0.1.2" :major))))

      (testing "minor version"
        (is (= "0.2.0" (sut/bump "0.1.2" :minor))))

      (testing "patch version"
        (is (= "0.1.3"
               (sut/bump "0.1.2")
               (sut/bump "0.1.2" :patch)))))

    (testing "semantic version -> semantic version"
      (testing "major version"
        (let [v (-> "0.1.2"
                    (sut/parse)
                    (sut/bump :major))]
          (is (= "1.0.0" (str v)))
          (is (= {:major 1
                  :minor 0
                  :patch 0
                  :pre-release nil
                  :build nil}
                 (into {} v)))))

      (testing "minor version"
        (let [v (-> "0.1.2"
                    (sut/parse)
                    (sut/bump :minor))]
          (is (= "0.2.0" (str v)))
          (is (= {:major 0
                  :minor 2
                  :patch 0
                  :pre-release nil
                  :build nil}
                 (into {} v)))))

      (testing "patch version"
        (let [v1 (-> "0.1.2"
                     (sut/parse)
                     (sut/bump))
              v2 (-> "0.1.2"
                     (sut/parse)
                     (sut/bump :patch))]
          (is (= "0.1.3" (str v1) (str v2)))
          (is (= {:major 0
                  :minor 1
                  :patch 3
                  :pre-release nil
                  :build nil}
                 (into {} v1)
                 (into {} v2))))))))
