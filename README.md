Camunda demo
============

Some unstructured code for playing with various aspects of Camunda. This
is unlikely to make any sense without a walkthrough from me, as it's just
stuff I was playing with at the time.

There are some sample actions, which tie into the Camunda BPMN documents 
you'll find in the main/resources folder.

This was all written in Spring 2019 (Feb/Mar/April), so may be out of date by now.

TestAction
----------

Just an implementation of the Camunda documented example.

poc
---

A start at a practical attempt to implement something that might be useful within the
context of the project I was working on at the time.

api
---

An exploratory attempt to use the Camunda history API to find
out about processes that are/have run, in the api package folder.


Running Notes
-------------

All the code assumes you have a local instance of Camunda running -
 I used the Docker image (and corresponding Docker PostgreSQL)


