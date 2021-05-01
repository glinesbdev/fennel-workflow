(ns fennel-workflow.core
  (:require [fennel-workflow.cli :refer [exit validate-args]]
            [fennel-workflow.fennel :refer [write-files]]
            [fennel-workflow.lua-format :refer [format-files]])
  (:gen-class))

(defn -main [& args]
  (let [{:keys [exit-message ok?] {:keys [path out format]} :options} (validate-args args)]
    (when exit-message
      (exit (if ok? 0 1) exit-message))
    (write-files path out)
    (when format
      (format-files out))))

(-main "-p" "/cfiles" "-o" "/ctest")
