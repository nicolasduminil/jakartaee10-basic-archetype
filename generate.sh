#!/bin/sh
mvn -B archetype:generate \
  -DarchetypeGroupId=fr.simplex-software.archetypes \
  -DarchetypeArtifactId=jakartaee10-basic-archetype \
  -DarchetypeVersion=1.0-SNAPSHOT \
  -DgroupId=com.exemple \
  -DartifactId=test
