(ns jira.core
 (:require [clj-http.client :as client]
           [clojure.data.json :as json]
           [clojure.edn :as edn]
           [jira.hicout :as hic])
(:gen-class))


; ----
; jira helper: constructs header, url, params, calls jira and retrieves per json the issues from body after search, if 200 is returned
; -----
(defn get-jira-issues [config jql fields]
  "retrieves issues by constructing header and params, calling jira on baseurl with rest-search for auth in project and getting body. May return empty map."
  (let [jurl (str (:baseurl config) (:rest-search config))
        myheader { :accept :json
                   :basic-auth (:auth config)
                   :query-params { "jql" jql
                                   "startAt" (:start-at config)
                                   "maxResults" (:maxresults config)
                                   "fields" fields
                                   }}
        response (client/get jurl myheader) ; this is the real call via http
        body (if (= (:status response) 200) (json/read-str (:body response)) {}) ]
    (if body
      (get body "issues")
      {})))

; ----
; Epics
; -----


(defn retrieve-epic-info [issue]
  "returns a single hash-map with key as key, summary, fixVersions (a hash-map itself)"
  (let [key (get issue "key")
       summary (get-in issue ["fields" "summary"])
       fix-versions (get-in issue ["fields" "fixVersions"]) ; a vector of hash-maps
       versionnames (clojure.string/join ", " (map #(get %1 "name") fix-versions)) ]
  (hash-map
    key ; the issue key as key
    (hash-map ; values with keys for each issue
      :summary summary  ; title aka summary
      :versions versionnames))))


(defn get-open-epics [config]
  "retrieves open epics in project as a collection of issues and associated information: release dates, summary"
  (let [jql (str "project = " (:project config)
                 " AND issuetype = Epos"
                 " AND resolution = Unresolved ORDER BY key ASC")
        issues (get-jira-issues config jql (:fields-epic config)) ]
    (if issues
      (reduce conj (map retrieve-epic-info issues)) ; join all the hashmaps into one
      {})))


; ----
; Stories related to one epic
; ----
(defn make-jql-stories [config epic-key]
  "create jql query"
  (str
    "project = " (:project config)
    " AND status in (Open, \"In Progress\", Reopened) AND resolution = Unresolved AND Epos-Verknüpfung = "
    epic-key
    " ORDER BY key ASC"))


(defn sum-epic-result [config issues]
  "returns a map containing :total :storypoints :not-estimated numbers from the issue collection"
  (let
    [sp-field-list (map #(get-in % ["fields" (:storypoint-field config)] nil) issues)] ; get fields.<storypoint field> from the issue
    (hash-map
      :total (count issues)
      :storypoints (int (reduce + (filter number? sp-field-list)))
      :not-estimated (count (filter nil? sp-field-list)))))


(defn get-open-stories [config epic] ; epic will be a vector [ "key" { key val, key val}] !!!!
  "for the given project and the epic, retrieves a map of issue-key and story point value as int"
  (let [epic-key (first epic) ; get the key
        jql (make-jql-stories config epic-key)
        issues (get-jira-issues config jql (:fields-story config))]
    (hash-map epic-key ; for the known key, only element though
              (conj (sum-epic-result config issues) ; the freshly calculated sums
                    (second epic))))) ; second element of vector -> summary, fixVersions



; ----
; report on all epics
; ----
(defn report-open-epics [config]
  "compile a simple report about each epic with sum of story points per epic"
  (let [epics (get-open-epics config)
        result (reduce conj
		       (for [epic epics]
			    (get-open-stories config epic)))]
			    (do
				(if (:spit-results config)
				    (spit (:results-edn config) result))
				(hic/make-table config result))))


; --------------------------------------------------
; get config from file
; --------------------------------------------------

(defn get-config [file]
  "reads the config from the given file, retrieves the auth info from the file indicated by the auth config setting"
  (let [orig-config (edn/read-string (slurp "config.edn")) ] ; read from file
       (assoc orig-config :auth (slurp (:mypath orig-config)))))     ; read secret file with username and password

; ----

(defn -main
  "Retrieves the epics for the config.edn supplied, retrieves the stories for each epic and sums up story points and not estimated stories - returns an html page"
  [& args]
  (let [config (get-config "config.edn")
       html (report-open-epics config)]
       (println html)))


; - remove the following for production !!!!!!
(def config (get-config "config.edn")) ; read from file




