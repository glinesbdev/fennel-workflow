(ns fennel-workflow.lua-format
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [fennel-workflow.io :refer [copy-file]]))

;; LuaFormatter executable and config.
(def lua-formatter (io/file "resources/3rd/lua-format"))
(def lua-config (io/file "resources/config/lua-config.yaml"))

;; Local formatter and config paths.
(def local-formatter "/tmp/lua-format")
(def local-config "/tmp/lua-config.yaml")

(defn- copy-formatter []
  (copy-file lua-formatter local-formatter)
  (copy-file lua-config local-config))

(defn format-file
  "Format the Lua file inline with the default configuration at a given [path]."
  [path]
  (println (str "Formatting " path))
  (copy-formatter)
  (sh local-formatter path "-i" (str "--config=" lua-config)))
