GEDCOM 5.5 to GEDCOM X Converter
================================

This utility converts a [GEDCOM 5.5 file](http://www.gedcomx.org/GEDCOM-5.5.1.pdf) to a
[GEDCOM X file](https://github.com/FamilySearch/gedcomx/blob/master/specifications/file-format-specification.md).
The utility leverages the open-source [GEDCOM 5.5 parsing library](https://github.com/DallanQ/GEDCOM)
contributed by Dallan Quass and the open-source [GEDCOM X file format writer](https://github.com/FamilySearch/gedcomx-fileformat-java)
contributed by FamilySearch. This converter follows the [Legacy GEDCOM Migration Path](http://www.gedcomx.org/Legacy-GEDCOM-Migration-Path.html)
as detailed by the [GEDCOM X project](http://www.gedcomx.org).

## Usage

Someday, [we may get a GUI for this utility](https://github.com/FamilySearch/gedcom5-conversion/issues/1).
But for now, you're going to have to run it from the command line:

1. [Install the Java Runtime Environment](http://java.com/en/download/index.jsp).
2. [Download the latest version of the utility]() and save it on your filesystem (e.g. `/tmp/gedcom-converter.jar`).
3.

## Developers

How to use this library (Maven coordinates)

## Building From Source

How to build from source...

## Status

There are still some things to be done. Here are some high-level notes on the status of this conversion tool.

### All Records

The following are not currently converted on all types of records:

* Notes (NOTE tag)
* Multimedia (OBJE tag)
* LDS Ordinances
* ID's such as RIN, RFN, REFN and AFN tags
* RESN tag
* AGE tag is not supported on the event structures
* Generic events (EVEN tag)

### Individual

The following are not currently converted on an individual records:

* Tags: ALIA ASSO
* Generic facts (FACT tag)

### Family

Families are converted into binary relationships (couple and parent-child). All tags are supported except the tags not supported on all records.

### Contributor

All tags are supported except the tags not supported on all records.

### Source

The following are not currently converted on an individual records:

* Tags: TEXT

### Repository

All tags are supported except the tags not supported on all records.
