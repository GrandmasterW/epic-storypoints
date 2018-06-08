(ns jira.core-test
  (:require [clojure.test :refer :all]
            [jira.core :refer :all]))

(def test-config (get-config "config.edn"))

(deftest sum-epic-result-test
  (testing "hashmap contains total, sp, not-estimated"
    (let
      [issues
       [{"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "103024",
         "self" "https://jira.server.com/rest/api/2/issue/93024", "key" "MYP-708", "fields" {(:storypoint-field test-config) nil}}
        {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "103707",
         "self" "https://jira.server.com/rest/api/2/issue/93707", "key" "MYP-730", "fields" {(:storypoint-field test-config) nil}}
        {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "103923",
         "self" "https://jira.server.com/rest/api/2/issue/93923", "key" "MYP-742", "fields" {(:storypoint-field test-config) 1.0}}
        {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "104818",
         "self" "https://jira.server.com/rest/api/2/issue/94818", "key" "MYP-790", "fields" {(:storypoint-field test-config) nil}}
        {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields", "id" "104820",
         "self" "https://jira.server.com/rest/api/2/issue/94820", "key" "MYP-791", "fields" {(:storypoint-field test-config) 2.0}}]
       result {:total 5 :storypoints 3 :not-estimated 3} ]
      (is
        (= result (sum-epic-result jira.core/config issues))))))


(deftest retrieve-epic-info-1-test
  (testing "ensure that summary and fixVersions are set accordingly"
	   (let
	       [i1 {"expand" "operations,versionedRepresentations,editmeta,changelog,renderedFields",
                    "id" "92204",
                    "self" "https://jira.server.com/rest/api/2/issue/92204",
                    "key" "MYP-154",
                    "fields"
                    { "summary" "Some summary text quit long isnt it?",
                     "fixVersions"
                     [
                      {"self" "https://jira.server.com/rest/api/2/version/34801",
                       "id" "34801",
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
                             {"self" "https://jira.server.com/rest/api/2/version/36100",
                              "id" "36100",
                              "description" "some release",
                              "name" "minor release 1",
                              "archived" false,
                              "released" false,
                              "releaseDate" "2018-07-01"}
                             {"self" "https://jira.server.com/rest/api/2/version/36100",
                              "id" "36100",
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
