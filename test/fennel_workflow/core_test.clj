(ns fennel-workflow.core-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [clojure.java.io :as io]
            [fennel-workflow.core :refer [-main]]))

(def input "/test/fennel_workflow/input")
(def output (str (System/getProperty "user.dir") "/test/fennel_workflow/output"))

(defn delete-directory-recursive [file]
  (when (.isDirectory (io/file file))
    (doseq [file-in-dir (.listFiles (io/file file))]
      (delete-directory-recursive file-in-dir)))
  (io/delete-file file))

(use-fixtures :once (fn [f]
                      (.mkdir (io/file output))
                      (f) 
                      delete-directory-recursive output)))

(deftest fennel-to-lua
  (testing "Can compile single files as well as directory structures"
    (-main "-p" input "-o" "/test/fennel_workflow/output" "-f")
    (is (contains? (set (.list (io/file output))) "fibonacci.lua"))
    (is (contains? (set (.list (io/file (str output "/pong")))) "movement.lua"))
    (is (contains? (set (.list (io/file (str output "/pong" "/tree")))) "walk.lua"))))


