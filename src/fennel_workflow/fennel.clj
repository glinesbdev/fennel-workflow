(ns fennel-workflow.fennel
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.java.shell :refer [sh]]
            [fennel-workflow.lua-format :refer [format-file]]
            [fennel-workflow.io :refer [copy-file write-file]]))

(def fennel
  "Get the current fennel executable."
  (io/file "resources/3rd/fennel.lua"))

(def local-fennel "/tmp/fennel")

(defn- copy-fennel []
  (copy-file fennel local-fennel))

(defn- replace-ext
  "Change file extension from .fnl to .lua."
  [file]
  (string/replace file #"(\.fnl)" ".lua"))

(defn- compile-cmd
  "Shell command to compile .fnl to .lua files."
  [path]
  (:out (sh local-fennel "--compile" path)))

(defn compile-files
  "Read all of the .fnl files inside the [path] and write it to [out].
   If [verbose] is true, the build process is displayed.
   If [format] is true, LuaFormatter will format the Lua files inline."
  [{:keys [path out verbose format]}]
  (copy-fennel)
  (let [files (->> path
                   (io/file)
                   (file-seq)
                   (filter #(.isFile (io/file %)))
                   (mapv #(.getAbsolutePath (io/file %))))]
    (doseq [file files]
      (let [out-path (replace-ext (string/replace file path out))]
        (when verbose
          (println (str "Writing " out-path)))
        (write-file out-path (compile-cmd file))
        (when format
          (format-file out-path))))))
