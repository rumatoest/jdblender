#!/bin/bash
#Creates war and copy it to delpoy dir on server
#Wrapper for some gradle tasks

usage() {
    echo "Usage: $0 project_name|all cycles_to_run"
    echo "	project_name - project to execute or type \"all\" to run on all projects"
    echo "	cycles_to_run - number of cycles to run (default 1)"
    echo "	test_set_factor - (default 1)"
}


JIT="jitc"
while getopts ":j" option; do
  case $option in
    j)
        JIT="NONE"
    ;;
  esac
done
shift $((OPTIND - 1))

if [[ -z "$1" ]]; then
    usage
    exit 0
fi

cd "$(dirname "$0")"

run_one() {
    CYCLES=$2
    echo "RUN $1 $CYCLES times"
    COUNTER=0
    ARG=${@:3}
    while [  $COUNTER -lt $CYCLES ]; do
        let COUNTER=COUNTER+1
        echo "CYCLE $COUNTER"
        ./gradlew :$1:run -Dargs="$ARG" -Pjit="$JIT" || break
    done
}

run_all() {
    COUNTER=0
    CYCLES=$1
    echo "RUN ALL $CYCLES times"
    ARG=${@:2}
    while [ $COUNTER -lt $CYCLES ]; do
        let COUNTER=COUNTER+1
        echo "CYCLE $COUNTER"
        ./gradlew run -Dargs="$ARG" -Pjit="$JIT" || break
    done
}

CYCLES=1
if [ ! -z "$2" ]; then
    let CYCLES=$(($2))
fi

ARGS=""
if [ ! -z "$3" ]; then
    ARGS="-f $3"
fi

if [ "$1" == "all" ]; then
    run_all $CYCLES $ARGS
else
    run_one $1 $CYCLES $ARGS
fi 
