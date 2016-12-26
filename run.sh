#!/bin/bash
# This is the only way to correct run JDBLENDER tests

help() {
    echo "
   __  ___  ___  __    ___  _  _  ___  ___  ___  
  (  )(   \(  ,)(  )  (  _)( \( )(   \(  _)(  ,) 
 __)(  ) ) )) ,\ )(__  ) _) )  (  ) ) )) _) )  \ 
(___/ (___/(___/(____)(___)(_)\_)(___/(___)(_)\_)

Usage: $0 [options] implementation/s
Argument:
  implementation/s  Implementation to run or comma separated list (type all to run all)

Options:
  -t number         Times to run each test implementation (by default 1)

  -r number         Reduce tests data sets up to N times if possible

  -d                Disable JIT

  -p port           Enable local profiler connection at specific port

  -j string         Additional JVM arguments

  -f flags          Specific test flags. Example: -F \"h2ds;hibernate=cache,jta;caynne=fast\"
                    Flags are just strings delimited by comma.
                    There are flag groups, that are delimited by semicolon.
                    Flags group related to some implemntation must have prefix like \"mybatis=f1,f2,f3\"
                    otherwise such flags will be treated as global.
                    There are no global flags yet available.
"
}


start_db() {
    db_start_command &
    PID=$!
}    
    

run_test() {
    JAR="${APP_DIR}/$1/build/libs/${1}.jar"
    OUT="${APP_DIR}/report-${1}.csv"
    
    if [ $INIT -eq 0 ]; then
	#Compile project here
	echo "COMPILE $1"
        ./gradlew ":${1}:jar"
    fi
    
    echo "RUN: $1"
    #echo "java $JVM -jar $JAR $OUT $REDUCE $FLAGS"
    java $JVM -jar $JAR $OUT $REDUCE $FLAGS
}


INIT=0
run_tests() {
    if [ $CYCLES -gt 1 ]; then
	echo "WILL RUN $CYCLES CYCLES"
    fi
    COUNTER=0
    while [ $COUNTER -lt $CYCLES ]; do
        let COUNTER=COUNTER+1
        echo "CYCLE $COUNTER"
	for i in $(echo $1 | sed "s/,/ /g")
	do
	    run_test $i || break
	done
	INIT=1
    done
}


cd "$(dirname "$0")"

#INITIALIZE DEFAULTS
HELP=0
CYCLES=1
REDUCE=1
ALL="jdbc,springjdbc,mybatis,jooq,cayenne,eclipse-link,hibernate,springdata"
JVM="-XX:CompileThreshold=10000 "
FLAGS=""
APP_DIR="$(dirname "$0")"


while getopts ":dp:t:r:f:h" opt; do
    case $opt in
	h)
	    HELP=1
	;;
        d)
            JVM="${JVM} -Djava.compiler=NONE "
	    export JDB_JIT="FALSE"
        ;;
	p)
	    if [ -z "$OPTARG" ]; then
	        echo  -e "\n ERROR: Invalid -p port value"
	        HELP=-1
	    else
		export JDB_PROFILE="TRUE"
	        JVM="$JVM -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$OPTARG -Dcom.sun.management.jmxremote.local.only=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false "
	    fi
	;;
	t)
	    if [ -z "$OPTARG" ]; then
	        echo  -e "\n ERROR: Invalid -t argument value"
	        HELP=-1
	    else
	        let CYCLES=$(($OPTARG))
	    fi
	;;
	r)
	    if [ -z "$OPTARG" ]; then
	        echo  -e "\n ERROR: Invalid reduce value"
	        HELP=-1
	    else
	        let REDUCE=$(($OPTARG))
	    fi
        ;;
	f)
	    if [ -z "$OPTARG" ]; then
	        echo  -e "\n ERROR: Invalid flags"
	        HELP=-1
	    else
	        FLAGS="$OPTARG"
	    fi
        ;;
	*)
	    echo  -e "\n ERROR: Invalid arguments!!!"
	    HELP=-1
	;;
    esac
done
shift $((OPTIND - 1))

if [[ -z "$1" ]]; then
    echo  -e "\n ERROR: Implementation argument is not specified!!!"
    HELP=-1
fi


if [[ 0 -ne $HELP ]]; then
    help
    exit 0;
else
    if [ "$1" == "all" ]; then
        run_tests $ALL
    else
	run_tests $1
    fi
fi

echo "ERROR"
    exit 1;

## OLD CODE
    
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
