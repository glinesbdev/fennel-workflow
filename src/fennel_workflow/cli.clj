(ns fennel-workflow.cli
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-p" "--path PATH" "Path of .fnl files to be compiled."
    :default "Default: ./src"
    :parse-fn #(str (System/getProperty "user.dir") (or % "/src"))]
   ["-o" "--out PATH" "Path where the .lua files will get compiled to."
    :default "Default: ./build"
    :parse-fn #(str (System/getProperty "user.dir") (or % "/build"))]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Fennel to Lua workflow utility."
        ""
        "Usage: program-name options"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn validate-args [args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) ;; help => exit OK with usage summary
      {:exit-message (usage summary) :ok? true}
      errors ;; errors => exit with description of errors
      {:exit-message (error-msg errors)}
      true ;; have nothing else to check to satisfy cond
      {:options options}
      :else ;; failed custom validation => exit with usage summary
      {:exit-message (usage summary)})))
