# Introduction
A simple `grep` application built using Java 1.8. The app searches for a regex text pattern recursively in a given directory, 
and output matched lines to files. The app takes in three arguments

```
USAGE: java -cp grep.jar {regex} {rootPath} {outFile}
    - regex: a special text string for describing a search pattern
    - rootPath: root directory path
    - outFile: output file name and path
```

Similar to bash script below

```bash
egrep -r {regex} {rootpath} > {outFile}
```

The program can be downloaded from Github and compiled on your local computer or server using `Maven`. Adjustments can be made to `pom.xml`.

`mvn clean build compile`

An Image from Docker Hub has the compiled `grep.jar` file uploaded and be pulled using 

`docker pull inthavo2/grep`

# Quick Start

1. Docker
```bash
# pull docker image from Docker Hub
docker pull inthavo2/grep

# run docker container
docker run --rm -v {rootPath} -v {outFile} inthavo2/grep {regex} {rootPath} {outFile}
```

2. Maven
```bash
# build package using Maven
mvn clean build package

# run java jar file
java -cp target/grep-1.0-SNAPSHOT.jar {regex} {rootPath} {outFile}
```

# Implementation

The Java `grep` app implemented using the Java `Stream` api for functional style manipulation of data collected from files within the folder given by `rootPath`.

## Pseudocode

```
matchedLines = []
for file in listFilesRecursively(rootDir):
    for line in readLines(file):
        if containsPattern(line):
            matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issues

Using Java Grep on large folders JVM can run out of memory.

Tests on the JVM memory can be done using the bash commands below (Requires clone repo)

```bash
# use -Xms and -Xmx flags to set current memory and max memory size on memory heap
# run java .jar file
java -Xms5m -Xmx5m -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp .*Romeo.*Juliet.* {rootPath} {outFile}
```

This issue can be solved by implementing a BufferReader and using the Stream API.

# Test

Tests were done manually by creating down data files and performing certain regexes on that folder.

sf4j logger was used for debugging purposes

# Deployment

## Docker

An image with the compiled java `.jar` file can be found on Docker Hub. The image can be pulled from the Docker Hub and ran as a container for easier distribution.

```bash
# pull docker image from Docker Hub
docker pull inthavo2/grep

# run docker container with arguments
docker run --rm -v {rootPath} -v {outFile} inthavo2/grep {regex} {rootPath} {outFile}
```

# Improvements

 - Java grep has difficultly reading from large files. We can fix this by implementing readLines function using a Buffer Object instead of an ArrayList