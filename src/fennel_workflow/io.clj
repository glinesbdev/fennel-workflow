(ns fennel-workflow.io
  (:require [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]))

(defn- has-file?
  "Check for file on local disk."
  [file]
  (.exists (io/file (str "/tmp/" file))))

(defn copy-file
  "Copy [project-file] to /tmp/[local-file] on local disk if it's not already there."
  [project-file local-file]
  (when (not (has-file? local-file))
    (io/copy (io/file project-file) (io/file local-file))
    (sh "chmod" "+x" local-file)))

(defn write-file
  "Write the current .lua file to disk.
   Respects folder structure and outputs the same structure."
  [out text]
  (io/make-parents out)
  (spit out text))
