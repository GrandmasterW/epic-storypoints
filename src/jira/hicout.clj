(ns jira.hicout
    (:require [hiccup.core :refer [html]]
	      [hiccup.page :refer [html5,include-css]])
  (:gen-class))

(defn make-table [config epic-map]
  "creates html via hiccup for each epic in the list"
  (html5 
	 [:head
	 (include-css (:css-file config))
	 [:title (:title config)]]
	 [:meta {:charset "UTF-8" }]

	 [:body

	 [:table 

	 [:thead
	 [:tr
	 (for [th (:th config)]
	      [:th th ])]] ; tr, thead

	 [:tbody
	 (for [e epic-map]
	      (let [evalues (val e)]
		   [:tr
		   [:td (key e) ]
		   [:td (:summary evalues)]
		   [:td (:versions evalues)]
		   [:td (:total evalues)]
		   [:td (:storypoints evalues)]
		   [:td (:not-estimated evalues)]  ] ; tr
		   )) ; let, for
	 ]; tbody
	 ] ; table
	 ] ; body
	 ))
