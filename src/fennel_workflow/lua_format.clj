(ns fennel-workflow.lua-format
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]))

;; LuaFormatter executable.
(def lua-formatter (.getPath (io/resource "3rd/lua-format")))

;; Format the full file path.
(defn format-file-path [path file]
  (str path "/" file))

;; Format the Lua files inline with the default configuration.
(defn format-files [path]
  (let [files (.list (io/file path))]
    (doseq [file files]
      (sh lua-formatter
          (format-file-path path file)
          "-i"
          (str "--config=" (.getPath (io/resource "config/lua-config.yaml")))))))
