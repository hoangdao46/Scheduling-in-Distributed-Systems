#!/bin/bash
# make sure you have your client and ds-sim (ds-server and ds-client) all in the same directory and test configuration files in configs directory 
# to kill multiple runaway processes, use 'pkill runaway_process_name'
# For the Java implementation, use the following format: ./demoS1Final.sh [Java specific arugment...] [-n] your_client.class [your client specific argument...]
configDir="./S1DemoConfigs"
diffLog="stage1.diff"

if [[ ! -d $configDir ]]; then
	echo "No $configDir found!"
	exit
fi

if [[ -f $configDir/$diffLog ]]; then
	rm $configDir/*.log
	rm $configDir/$diffLog
fi

if [[ $# -lt 1 ]]; then
	echo "Usage: $0 [Java specific arugment...] [-n] your_client.class [your client specific argument...]"
	exit
fi

if [[ $@ != *".class"* ]]; then
	echo "No class file or .class extension missing!"
	exit
fi

#if [ ! -f $1 ]; then
	#echo "No $1 found!"
	#echo "Usage: $0 your_client [user-specified argument...]"
	#exit
#fi

trap "kill 0" EXIT

newline=""

args=$@
for arg in $@; do
	if [[ $arg == "-n" ]]; then
		args=$(sed 's/-n//' <<< $@)
		newline="n"
		break
	fi
done

for arg in $args; do
	if [[ $arg == *".class" ]]; then
		yourClient=$arg
		echo $arg
		break
	fi
	javaArgs+=" $arg"
done

isCArg=0
for arg in $args; do
	if [[ $isCArg -eq 1 ]]; then
		clientArgs+=" $arg"
	fi
	if [[ $arg == *".class" ]]; then
		isCArg=1
	fi
done

if [[ ! -f $yourClient ]]; then
	echo "No $1 found!"
fi

for conf in $configDir/*.xml; do
	echo "$conf"
	echo ----------------
	echo "running the reference implementation (./ds-client)..."
	sleep 1
	if [[ $newline == "n" ]]; then
		./ds-server -c $conf -v brief -n > $conf.ref.log&
		sleep 4
		./ds-client -n
	else
		./ds-server -c $conf -v brief > $conf.ref.log&
		sleep 4
		./ds-client
	fi
	
	echo "running your implementation ($yourClient)..."
	sleep 2
	if [[ $newline == "n" ]]; then
		./ds-server -c $conf -v brief -n > $conf.your.log&
	else
		./ds-server -c $conf -v brief > $conf.your.log&
	fi
	sleep 4
	java $javaArgs $(sed 's/\.class//' <<< $yourClient)$clientArgs
	sleep 1
	diff $conf.ref.log $conf.your.log > $configDir/temp.log
	if [[ -s $configDir/temp.log ]]; then
		echo NOT PASSED!
	elif [ `wc -c < $conf.ref.log` -eq 0 -a `wc -c < $conf.your.log` -eq 0 ]; then
		echo "NOT PASSED (no log files)!"
	else
		echo PASSED!
	fi
	echo ============
	sleep 1
	cat $configDir/temp.log >> $configDir/$diffLog
done

echo "testing done! check $configDir/$diffLog"

