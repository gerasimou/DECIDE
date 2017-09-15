#!/bin/bash

#  launch.sh
#
#  Created by Simos Gerasimou on 08/05/2014.
#  Copyright (c) 2014 SPG. All rights reserved.
PRISM_DIR=PrismServer_lib

export DYLD_LIBRARY_PATH="$PRISM_DIR":$DYLD_LIBRARY_PATH

#parameters port number, model filename, properties filename should be given at the end of the following command
java -jar PrismServer.jar "920$1" #input/test.sm input/test.csl


###################
#old
#PRISM_CLASSPATH="$PRISM_DIR"/lib/prism.jar:"$PRISM_DIR"/classes:"$PRISM_DIR":"$PRISM_DIR"/lib/pepa.zip:"$PRISM_DIR"/lib/*

#export DYLD_LIBRARY_PATH="$PRISM_DIR"/lib:$DYLD_LIBRARY_PATH

#If you're running a jar file with java -jar, the -classpath argument is ignored. You need to set the classpath in the manifest file of your jar,
#java -classpath "$PRISM_CLASSPATH" -jar PrismServer.jar


#java -Djava.library.path=$PRISM_DIR/lib -classpath "$PRISM_CLASSPATH" -jar PrismServer.jar
