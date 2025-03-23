# Distributed Systems Assignment 1

## Overview

This project implements a distributed system for generating sequence numbers. It involves a server and multiple clients, utilizing RMI for communication.

## Build and Run

1.  **Build:**
    *   Ensure you have Apache Ant installed.
    *   Run `ant build` in the project directory.  This will create the `dist/dspa1.jar` file.

2.  **Run:**
    *   Execute the `run.sh` script.  This script takes the number of sequence number calls as an argument.
    *   Example: `./run.sh 100` will run the program with 100 calls to `getSequenceNumber`.

## Project Structure

*   `src/`: Contains the Java source code.
*   `build/`: Contains compiled class files.
*   `dist/`: Contains the executable JAR file and external dependencies.
*   `run.sh`:  Script to build and run the project.
*   `build.xml`: Ant build file.
*   `report.md`: Contains the project report.
*   `practical-assignment-1-ezpz.iml`: IntelliJ project file.

## Files

*   `Client.java`: Client-side logic.
*   `ClientServer.java`: Main class to determine if the program should run as a client or server.
*   `Server.java`: Server-side logic.
*   `ServerImplementation.java`: Implementation of the server interface.
*   `ServerInterface.java`: Defines the server's remote interface.
*   `Util.java`: Utility class.

## Dependencies

*   slf4j-api-2.0.16.jar
*   slf4j-simple-2.0.16.jar

## Testing

*   The project can be tested by running the `run.sh` script with different numbers of clients and sequence number calls.
*   The `report.md` file contains the results of the tests.
