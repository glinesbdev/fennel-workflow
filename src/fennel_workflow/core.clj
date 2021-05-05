(ns fennel-workflow.core
  (:require [fennel-workflow.cli :refer [exit validate-args]]
            [fennel-workflow.fennel :refer [compile-files]])
  (:gen-class))

(defn -main [& args]
  (let [{:keys [exit-message ok?] {:keys [path out format verbose]} :options} (validate-args args)]
    (when exit-message
      (exit (if ok? 0 1) exit-message))
    (compile-files path out verbose format)
    (System/exit 0)))
