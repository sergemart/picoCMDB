#!/usr/bin/env python
# Changes primary keys names from "PRIMARY" to "PK_[table name]" to make them unique
# Needed when diff is generated from MySQL databases, in which "PRIMARY" is mandatory name for any primary key.
import os, sys
#from xml.etree import ElementTree as ET
from lxml import etree as ET # unlike xml.etree, allows to keep XML comments


basename = os.path.basename(sys.argv[0])
nargs = len(sys.argv)
if not nargs == 2:
    print("usage: %s changelog_to_be_fixed" %basename)
    exit(1)
else:
    print("%s: Current directory: %s" % (basename, os.getcwd()))
    print("%s: File to process: %s" % (basename, sys.argv[1]))

sFileToFix = sys.argv[1]

# required by xml.etree, to not overwrite namespaces
#ET.register_namespace('', "http://www.liquibase.org/xml/ns/dbchangelog")
#ET.register_namespace('xsi', "http://www.w3.org/2001/XMLSchema-instance")

# 
xmlTreeToFix = ET.parse(sFileToFix)
xmlRootToFix = xmlTreeToFix.getroot()
namespaces = {"ns": "http://www.liquibase.org/xml/ns/dbchangelog"} # etree requires to explicit pass namespaces to make XPath search
xmlElementsToFix = xmlRootToFix.findall(".//ns:addPrimaryKey[@constraintName='PRIMARY']", namespaces) # given elements at all levels having given attr value

for xmlElement in xmlElementsToFix:
    xmlElement.set("constraintName", "PK_" + xmlElement.get("tableName"))

xmlTreeToFix.write(sFileToFix)

print("%s: Done." %basename)
exit()