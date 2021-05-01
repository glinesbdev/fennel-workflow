(ns fennel-workflow.fennel
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.java.shell :refer [sh]]))

(def fennel (->> (reverse (.list (io/file "resources/3rd")))
                 (filter #(re-seq #"^fennel(-\d){3}\.lua$" %))
                 first))

(defn- replace-ext [file]
  (string/replace file #"(\.fnl)" ".lua"))

(defn- compile-cmd [path]
  (:out (sh fennel "--compile" path)))

(defn- compile-files [path]
  (map (fn [tree]
         (let [abs-path (.getAbsolutePath (io/file tree))]
           (when (.isFile (io/file abs-path))
             {:file-path (replace-ext abs-path)
              :text (compile-cmd abs-path)})))
       (file-seq (io/file path))))

(defn write-files [path output]
  (doseq [{:keys [file-path text]} (filter (complement nil?) (compile-files path))]
    (let [output-name (str (string/replace file-path path output))]
      (io/make-parents output-name)
      (spit output-name text))))
