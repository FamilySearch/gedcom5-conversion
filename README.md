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
2. [Download the latest version of the utility](https://repository-gedcom.forge.cloudbees.com/snapshot/org/gedcomx/gedcom5-conversion/1.0.0.M1/gedcom5-conversion-1.0.0.M1-full.jar) and save it on your filesystem (e.g. `/tmp/gedcom-converter.jar`). (Be sure to download the "full" jar, otherwise you'll get a "no main manifest attribute" error message.)
3. Go find a GEDCOM file and put it on your filesystem (e.g. `/tmp/my.ged`).
4. Run the command:

```
$ java -jar /tmp/gedcom-converter.jar -i /tmp/my.ged -o /tmp/my.gedx
```

Assuming the above command is successful, the GEDCOM X file will be written to `/tmp/my.gedx`. Since the file
is based on the ZIP file format, you can open it up with your favorite unzip program to see the contents.

## Developers

This library is a [Maven](http://maven.apache.org/)-based project. It's built out on
[Cloudbees](http://www.cloudbees.com/) ([release build](https://gedcom.ci.cloudbees.com/job/gedcom-to-gedcomx-converter-release/),
[snapshot build](https://gedcom.ci.cloudbees.com/job/gedcom-to-gedcomx-converter/)). Here are the maven coordinates:

```xml
<dependency>
  <groupId>org.gedcomx</groupId>
  <artifactId>gedcom5-conversion</artifactId>
  <version>${gedcom5-conversion.version}</version>
</dependency>
```

## Building From Source

1. Clone the repo.
2. `mvn clean install`

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
