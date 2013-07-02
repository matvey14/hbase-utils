hbase-utils
===========
hbase-utils contains utilities to help with administration of hbase.

run_region_splitter.sh
----------------------
This is a simple utility for managing splits on HBase. Using this you can schedule HBase splits to run as a cronjob.

Sample Usage:
```
./run_region_splitter.sh -s 10 -r
```
This will go through all of your HBase regions, and split any region that is bigger than 10Gb. The "-r" argument tells it to actually do splits. Without "-r", the script defaults to "dry-run" mode, so it'll go through each region and show you what will happen, but won't actually do any splitting.

Building from Source
----------------------
- Make sure you have gradle installed. You can download it from http://www.gradle.org/downloads
- Execute "gradle zip". This will create a distribution containing the jar and the shell scripts.

