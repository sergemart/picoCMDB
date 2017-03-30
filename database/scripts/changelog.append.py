#!/usr/bin/env python
# Appends a Liquibase changelog to another one
import os, sys
#from xml.etree import ElementTree as ET
from lxml import etree as ET # compared to xml.etree, allows to keep XML comments

basename = os.path.basename(sys.argv[0])
nargs = len(sys.argv)
if not nargs == 3:
    print("usage: %s changelog_to_be_appended changelog_to_append" %basename)
    exit(1)
else:
    print("%s: Current directory: %s" % (basename, os.getcwd()))
    print("%s: Changelog to be appended: %s" % (basename, sys.argv[1]))
    print("%s: Changelog to append: %s" % (basename, sys.argv[2]))

sFileToBeAppended = sys.argv[1]
sFileToAppend = sys.argv[2]

# required by xml.etree, to not overwrite namespaces
#ET.register_namespace('', "http://www.liquibase.org/xml/ns/dbchangelog")
#ET.register_namespace('xsi', "http://www.w3.org/2001/XMLSchema-instance")

# 
xmlTreeToBeAppended = ET.parse(sFileToBeAppended)
xmlRootToBeAppended = xmlTreeToBeAppended.getroot()
xmlElementListToBeAppended = xmlRootToBeAppended.getchildren()

#
xmlTreeToAppend = ET.parse(sFileToAppend)
xmlRootToAppend = xmlTreeToAppend.getroot()

for xmlElementToBeAppended in xmlElementListToBeAppended:
    xmlRootToAppend.append(xmlElementToBeAppended)

xmlTreeToAppend.write(sFileToAppend)

print("%s: Done." %basename)
exit()