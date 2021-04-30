(ns fennel-workflow.fennel
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.java.shell :refer [sh]]))

(def fennel (->> (reverse (.list (io/file "resources")))
                 (filter #(re-seq #"^fennel(-\d){3}\.lua$" %))
                 first))

(defn- strip-ext [file]
  (string/replace file #"(\.fnl)" ""))

(defn- output-file-name [output filename]
  (->> (str filename ".lua")
       (str output "/")))

(defn- format-file-path [path file]
  (str path "/" file))

(defn- compile-cmd [path file]
  (sh fennel "--compile" (format-file-path path file)))

(defn- compile-files [path]
  (map (fn [file]
         {:filename (strip-ext file)
          :text (:out (compile-cmd path file))})
       (.list (io/file path))))

(defn write-files [path output]
  (doseq [{:keys [filename text]} (compile-files path)]
    (with-open [w (io/writer (output-file-name output filename) :append true)]
      (.write w text))))
