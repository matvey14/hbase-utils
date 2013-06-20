#!/bin/bash

# USAGE: ./run_region_splitter.sh -s 10 -r

usage() {
cat << EOF
usage: $0 [OPTIONS] 

Runs region splitter based on maximum file size.

OPTIONS:
   -s    Maximum region size in GB
   -r    Actually do splits for Real. If not specified, will run in "dry-run" mode.
EOF
}

DRY_RUN=true

while getopts "s:r" OPTION
do
     case $OPTION in
         s)
             MAX_REGION_SIZE=$OPTARG
             ;;
         r)
             DRY_RUN=false
             ;;
     esac
done

if [[ -z "$MAX_REGION_SIZE" ]]
then
	usage
	exit 1
fi

CP="`hbase classpath`:hbase-utils-1.0.jar"

java -cp "$CP" -DmaxSplitSize="$MAX_REGION_SIZE" -DdryRun="$DRY_RUN" com.demdex.keystone.hbase.tools.HBaseRegionSplitter

