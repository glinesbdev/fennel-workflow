(ns fennel-workflow.core
  (:require [fennel-workflow.cli :refer [exit validate-args]]
            [fennel-workflow.fennel :refer [write-files]])
  (:gen-class))

(defn -main [& args]
  (let [{:keys [options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (doseq [key (map key options)]
        (cond
          (= :path key) (write-files (key options) (:out options)))))))
