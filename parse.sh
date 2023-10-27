#!/bin/bash

ABS_JAR='target/abs-1.0-SNAPSHOT.jar'

# Compile if necessary
if [ ! -e "${ABS_JAR}" ]; then
	mvn clean install
fi

# TODO: Pass all arguments to the jar and render the dot file it outputs
java -cp "${ABS_JAR}:/home/john/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.15.1/jackson-databind-2.15.1.jar:/home/john/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.15.1/jackson-core-2.15.1.jar:/home/john/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.15.1/jackson-annotations-2.15.1.jar" abs.compiler.parser.PrecedenceClimbingParserV2 "$@"

# Render the dot file as a png
dot -Tpng -o tree.png tree.dot
