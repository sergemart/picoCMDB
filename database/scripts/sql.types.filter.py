#!/usr/bin/env python
# Replaces DBMS-bound SQL types with generic (DBMS-unbound) ones
import os, sys

nargs = len(sys.argv)
if not nargs == 3:
    print("usage: %s file_to_process json_with_filter" %os.path.basename(sys.argv[0]))
    exit(1)
else: print("Current directory: " + os.getcwd() + "\nFile to process: " + sys.argv[1] + "\nJSON with filter: " + sys.argv[2])

sFileToProcess = sys.argv[1]
sFilter = sys.argv[2]

# read file into string and close it
with open(sFileToProcess) as fileToProcess:
    sData = fileToProcess.read()

# get dictionary with filter pairs from file
with open(sFilter) as fileFilter:
    dictFilter = eval(fileFilter.read())

for key, value in dictFilter.items():
    # search keys and replace them to their values, all preceded with double-quotes to avoid wrong replaces
    sData = sData.replace('"' + key, '"' + value)

with open(sFileToProcess, "w") as fileToProcess:
    fileToProcess.write(sData)

exit()