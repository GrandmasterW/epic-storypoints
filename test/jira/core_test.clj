(ns jira.core-test
  (:require [clojure.test :refer :all]
            [jira.core :refer :all]))

(def test-config
     {
 :mypath "/Users/johndoe/.jiraauth"
 :baseurl "https://jira.server.com"
 :rest-search "/rest/api/2/search"
 :start-at 0
 :maxresults 100
 :project "MYP"
 :auth "username:password"
 :storypoint-field "customfield_1103"
 :fields-epic "id,key,summary,fixVersions"
 :fields-story "id,key,customfield_1103"
 :epic-link-jql " AND Epos-Verknüpfung = "
 :css-file "mystyle.css"
 :spit-results true
 :results-edn "data/results.edn"
 :title "&Uuml;bersicht" ; html title
 :th ; table header labels
     [ "Vorgang" ; issue
      "Titel" ; summary 
      "Release" ; release 
      "Offene Vorg&auml;nge" ; open-issues 
      "Offene Storypoints" ; open-storypoints 
      "Nicht gesch&auml;tzte Vorg&auml;nge" ; not-estimated
      ]
})

(deftest sum-epic-result-test
  (testing "hashmap contains total, sp, not-estimated"
    (let
      [issues
       [{"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "24",
         "self" "https://jira.server.com/rest/api/2/issue/24", "key" "MYP-8", "fields" {(:storypoint-field test-config) nil}}
        {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "7",
         "self" "https://jira.server.com/rest/api/2/issue/7", "key" "MYP-30", "fields" {(:storypoint-field test-config) nil}}
        {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "23",
         "self" "https://jira.server.com/rest/api/2/issue/23", "key" "MYP-42", "fields" {(:storypoint-field test-config) 1.0}}
        {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "18",
         "self" "https://jira.server.com/rest/api/2/issue/18", "key" "MYP-19", "fields" {(:storypoint-field test-config) nil}}
        {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "20",
         "self" "https://jira.server.com/rest/api/2/issue/20", "key" "MYP-91", "fields" {(:storypoint-field test-config) 2.0}}]
       result {:total 5 :storypoints 3 :not-estimated 3} ]
      (is
        (= result (sum-epic-result test-config issues))))))


(deftest retrieve-epic-info-1-test
  (testing "ensure that summary and fixVersions are set accordingly"
	   (let
	       [i1 {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields",
                    "id" "204",
                    "self" "https://jira.server.com/rest/api/2/issue/204",
                    "key" "MYP-154",
                    "fields"
                    { "summary" "Some summary text quit long isnt it?",
                     "fixVersions"
                     [
                      {"self" "https://jira.server.com/rest/api/2/version/801",
                       "id" "801",
                       "description" "final release you know",
                       "name" "final release",
                       "archived" false,
                       "released" false,
                       "releaseDate" "2018-11-30"}]
                     }}

                r1 {"MYP-154" {:summary "Some summary text quit long isnt it?" :versions "final release"}}  ]
             (is
              (= r1 (retrieve-epic-info i1))))))

(deftest retrieve-epic-info-2-test
  (testing "ensure that summary and fixVersions are set accordingly"
    (let
        [i2 {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields",
             "id" "4711",
             "self" "https://jira.server.com/rest/api/2/issue/4711",
             "key" "MYP-4711",
             "fields"
             {"summary" "some feature",
              "fixVersions" [
                             {"self" "https://jira.server.com/rest/api/2/version/610",
                              "id" "610",
                              "description" "some release",
                              "name" "minor release 1",
                              "archived" false,
                              "released" false,
                              "releaseDate" "2018-07-01"}
                             {"self" "https://jira.server.com/rest/api/2/version/610",
                              "id" "610",
                              "description" "VX",
                              "name" "VX",
                              "archived" false,
                              "released" false,
                              "releaseDate" "2018-12-31"}]
              }}
         r2 {"MYP-4711" {:summary "some feature" :versions "minor release 1, VX"}}	       ]
      (is
       (= r2 (retrieve-epic-info i2))))))

(deftest retrieve-epic-info-3-test
  (testing "ensure that summary and fixVersions are set accordingly"
    (let
        [i2 {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields",
             "id" "4711",
             "self" "https://jira.server.com/rest/api/2/issue/4711",
             "key" "MYP-4711",
             "fields"
             {"summary" "Some feature",
              "fixVersions" [
                             ]
              }}
         r2 {"MYP-4711" {:summary "Some feature" :versions ""}}	       ]
      (is
       (= r2 (retrieve-epic-info i2))))))

;; not yet implemented
(deftest get-jira-issues-test
  (testing "get-jira-issues config jql fields"
	   (is (= 1 0))))

(deftest get-open-epics-test
  (testing " get-open-epics config"
	   (is (= 1 0))))

(deftest make-jql-stories-test
  (testing " make-jql-stories config epic-key"
	   (is (= 1 0))))

(deftest get-open-stories-test
  (testing " get-open-stories config epic"
	   (is (= 1 0))))

(deftest report-open-epics-test
  (testing " report-open-epics config"
	   (is (= 1 0))))

(deftest get-config-test
  (testing " get-config file"
	   (is (= 1 0))))

