(ns fennel-workflow.cli
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]))

(def user-dir
  "Helper to get the directory this program was run in."
  (System/getProperty "user.dir"))

(def cli-options
  "Options that show when either an error is encountered with the given options or when -h or --help is used."
  [["-p" "--path PATH" "Path of .fnl files to be compiled."
    :default "src"
    :parse-fn #(str user-dir (or % "/src"))]
   ["-o" "--out PATH" "Path where the .lua files will get compiled to."
    :default "out"
    :parse-fn #(str user-dir (or % "/out"))]
   ["-v" "--verbose" "Shows the build output." :default false]
   ["-f" "--format" "Formats the lua output with LuaFormatter" :default false]
   ["-h" "--help"]])

(defn usage
  "Outputs the usage of the program."
  [options-summary]
  (->> ["Fennel to Lua workflow utility."
        ""
        "Usage: program-name options"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg
  "Error message if an error is encountered."
  [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn validate-args
  "Validates the arguments given to the program.
   If [--help] is passed then exit the program and show usage summary.
   If an error occurs then the program will exit with description of errors."
  [args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}
      errors 
      {:exit-message (error-msg errors)}
      true
      {:options options}
      :else 
      {:exit-message (usage summary)})))
