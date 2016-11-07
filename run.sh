#!/bin/bash
#Creates war and copy it to delpoy dir on server
#Wrapper for some gradle tasks

usage() {
    echo "Usage: $0 project_name|all cycles_to_run"
    echo "	cycles_to_run - number of cycles to run test default to 1"
    echo "	project_name - project to execute or type \"all\" to run on all projects"
}

if [ -z $1 ]
then
    usage
    exit 0
fi

cd "$(dirname "$0")"

run_one() {
    CYCLES=$(($2))
    echo "RUN $1 $CYCLES times"
    COUNTER=0
    while [  $COUNTER -lt $CYCLES ]; do
        let COUNTER=COUNTER+1
        echo "CYCLE $COUNTER"
        ./gradlew :$1:run

        # Fix to exit loop on Ctrl+C
        test $? -gt 128 && break;
    done
}

run_all() {
    COUNTER=0
    CYCLES=$((1))
    echo "RUN ALL $CYCLES times"
    while [  $COUNTER -lt $CYCLES ]; do
        let COUNTER=COUNTER+1
        echo "CYCLE $COUNTER"
        ./gradlew run

        # Fix to exit loop on Ctrl+C
        test $? -gt 128 && break;
    done
}

if [ "$1" == "all" ]; then
    run_all ${2:-1}
else
    run_one $1 ${2:-1}
fi 