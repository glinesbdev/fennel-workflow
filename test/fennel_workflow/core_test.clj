(ns fennel-workflow.core-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [clojure.java.io :as io]
            [fennel-workflow.core :refer [-main]]))

(def input "/test/fennel_workflow/input")
(def output (str (System/getProperty "user.dir") "/test/fennel_workflow/output"))

(use-fixtures :once (fn [f] (f) (io/delete-file (str output "/fibonacci.lua"))))

(deftest fennel-to-lua
  (testing "Successfully compiles Fennel to Lua"
    (-main "-p" input "-o" "/test/fennel_workflow/output")
    (is (= (first (.list (io/file output))) "fibonacci.lua"))))
