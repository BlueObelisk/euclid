# CML Euclid
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.blueobelisk/euclid/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.blueobelisk/euclid)
[![Build Status](https://github.com/BlueObelisk/euclid/actions/workflows/maven.yml/badge.svg)](https://github.com/BlueObelisk/euclid/actions/workflows/maven.yml)

A library of numeric, geometric and XML routines

Euclid was written ca. 1994 as Java had no useful libraries then. Much of the
functionality is now present in Apache and other libraries and in an ideal world
Euclid maths and geometry could be replaced. However, there are additions that are valuable.

It's used a lot in CML tools (JUMBO, JUMBO-converters) and also AMI (for extracting semantics from PDFs).

## Releases

Instructions to increase the version:

```shell
mvn versions:set -DnewVersion=1.4-SNAPSHOT
```

Deploy to Sonatype with the following commands, for snapshots and releases respectively:

```sh1ll
mvn clean deploy
mvn clean deploy -P release
```
