(ns fennel-workflow.fennel
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.java.shell :refer [sh]]
            [fennel-workflow.lua-format :refer [format-file]]))

;; Get the current fennel executable. Versions will need to be updated
;; so there isn't a version hard coded here.
(def fennel (->> (reverse (.list (io/file "resources/3rd")))
                 (filter #(re-seq #"^fennel(-\d){3}\.lua$" %))
                 first))

;; Change file extension from .fnl to .lua
(defn- replace-ext [file]
  (string/replace file #"(\.fnl)" ".lua"))

;; Shell command to compile .fnl to .lua files
(defn- compile-cmd [path]
  (:out (sh fennel "--compile" path)))

;; Write the current .lua file to disk.
;; Respects folder structure and outputs the same structure.
(defn- write-file [path output format {:keys [file-path text]}]
  (let [output-path (str (string/replace file-path path output))]
    (io/make-parents output-path)
    (spit output-path text)
    (when format
      (format-file output-path))))

;; Read all of the .fnl files inside the `path` and write it to `output`.
(defn compile-files [path output verbose format]
  (keep (fn [tree]
          (let [abs-path (.getAbsolutePath (io/file tree))]
            (when (.isFile (io/file abs-path))
              (when verbose
                (println (str "Writing " (replace-ext abs-path))))
              (write-file path output format {:file-path (replace-ext abs-path)
                                              :text (compile-cmd abs-path)}))))
        (file-seq (io/file path))))
