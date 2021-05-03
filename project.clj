(defproject fennel-workflow "0.1.0"
  :description "A Fennel to Lua workflow"
  :url "https://github.com/glinesbdev/fennel-workflow"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "1.0.206"]]
  :main ^:skip-aot fennel-workflow.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
