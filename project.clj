(defproject jira "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jvm-opts ["--add-modules" "java.xml.bind"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [base64-clj "0.1.1"]
;                 [clj-http-lite "0.3.0"]
                 [clj-http "3.9.0"]
                 [hiccup "1.0.5"]]
  :main ^:skip-aot jira.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :source-paths ["src"])
