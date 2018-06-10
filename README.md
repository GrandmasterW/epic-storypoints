# Epic Story Points
An example in Clojure about how to compute storypoints from ATLASSIAN JIRA (R) Server via REST API - not suitable for productive usage! 

## Installation

Download from https://github.com/GrandmasterW/epic-storypoints

Copy the config.edn.example to config.edn - currently, filename is not
configurable... 

Change settings in config.edn to your system and your needs.

Most important: set up a secret file (unreadable to other people!),
containing only username:password - without any other characters! Make
sure, that it does not contain an trailing newline! 

## Usage

FIXME: explanation

Run directly in leiningen by:

	$ lein run

Or use the jar - currently, args are not supported. 

	$ java -jar jira-0.1.0-standalone.jar [args]

However, the program will print to stdout the resulting html file
contents. You might want to redirect the output to a file under UNIX /
MacOS:

	$ lein run > foo.html

The html file references mystyle.css (check config.edn), so have it in
the same directory. 

## Options

currently none

## Examples

See Usage

### Bugs

many... 

### Caution

This is a rather simple example - use it for your own experiments and
coding. It does not provide exception handling, suitable
configuration etc. - Feel free to add.

Furthermore, it is only tested with JIRA Server REST API - probably
not suitable for JIRA Cloud API.

Authentication is achieved by basic-authentication in each request -
no cookie or token handling yet.

Query strings will vary based on your customization of JIRA! So will
the name of the story point field. Make sure config.edn is correct.

Computation of epic fields should be improved: one cycle too many
here.

Tests are not implemented for most of the functions - please contribute if
possible. 

## License

Copyright © 2018 Werner Beckmann

Distributed under the Apache License v.2.0. Feel free to use, expand
and distribute to help other people! 
