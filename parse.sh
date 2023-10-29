#!/bin/bash

clear

ABS_JAR='target/abs-1.0-SNAPSHOT.jar'

# Compile if necessary
if [ ! -e "${ABS_JAR}" ]; then
	mvn clean install
fi

# TODO: Pass all arguments to the jar and render the dot file it outputs
java -cp target/classes abs.compiler.parser.PrecedenceClimbingParserV2 "$@"

# Render the dot file as a png
dot -Tpng -o tree.png tree.dot
