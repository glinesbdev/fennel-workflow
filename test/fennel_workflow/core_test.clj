(ns fennel-workflow.core-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [clojure.java.io :as io]
            [fennel-workflow.core :refer [-main]]))

(def input "/test/fennel_workflow/input")
(def out-path "/test/fennel_workflow/out")
(def out (str (System/getProperty "user.dir") out-path))

(defn delete-directory-recursive [file]
  (when (.isDirectory (io/file file))
    (doseq [file-in-dir (.listFiles (io/file file))]
      (delete-directory-recursive file-in-dir)))
  (io/delete-file file))

(use-fixtures :once (fn [f]
                      (.mkdir (io/file out))
                      (f)
                      (delete-directory-recursive out)))

(deftest fennel-to-lua
  (testing "Can compile single files as well as directory structures"
    (-main "-p" input "-o" out-path)
    (is (-> (str out "/fibonacci.lua")
            (io/as-file)
            .exists))
    (is (-> (str out "/pong/movement.lua")
            (io/as-file)
            .exists))
    (is (-> (str out "/pong/tree/walk.lua")
            (io/as-file)
            .exists))))
