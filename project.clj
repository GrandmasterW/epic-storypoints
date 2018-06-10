(defproject jira "0.1.0-SNAPSHOT"
  :description "An example how to compute story points for JIRA epics for JIRA Server REST API - to be used as a base for your own experiments, not suitable for production yet!"
  :url "http://www.atlassian.com"
  :license {:name "Eclipse Public License 2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :jvm-opts ["--add-modules" "java.xml.bind"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [base64-clj "0.1.1"]
                 [clj-http "3.9.0"]
                 [hiccup "1.0.5"]
		 ]
  :main ^:skip-aot jira.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :source-paths ["src"])
