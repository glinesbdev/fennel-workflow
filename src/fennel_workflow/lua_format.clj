(ns fennel-workflow.lua-format
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]))

;; LuaFormatter executable.
(def lua-formatter (.getPath (io/resource "3rd/lua-format")))

;; Format the Lua files inline with the default configuration.
(defn format-file [path]
  (sh lua-formatter path "-i" (str "--config=" (.getPath (io/resource "config/lua-config.yaml")))))
