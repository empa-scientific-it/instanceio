# OpenBIS Instance Initialiser

## What is it
This repository contains an *experimental* tool written in Kotlin which can export and import the schema (*master data*) from  openBIS instances using JSON files.
This software is in a very preliminary state, please feel free to open an issue or a PR if you find any problems.

## How to build the project
If you want to easily manage various Java versions, I recommend to use SDKMAN as a package manager.
### Setup envrionment
Follow these steps:
1. Install SDKMAN following the guide [here](https://sdkman.io/install)
2. Install java:
   ```shell
   sdk install java 19.0.1-open
   ```
3. Install Gradle:
   ```shell
   sdk install gradle 8.0
   ```
4. Install Kotlin:  
    ```shell
   sdk install kotlin
   ```
### Build and run
#### Using the Kotlin Application Plugin
To build the project, use gradle with the `installDist` task. This will create a local installation:
```shell
gradle installDist
```
The installation is in ` app/build/install/app/bin/app`

#### Building a fatJar
If you prefer, you can use the `jar` gradle task to build a *fat Jar*, this is a java archive which bundles all dependencies in one file that you can deploy anywhere.
To do so, use
```shell
gradle jar
```
The result is in `app/build/libs/app.jar`



## How to use the tool
Once you have built the project, you can use the tool to both import and export metadata (and data!) from an existing openBIS instance.

### Export Schema
To export the schema (*master data*) from an openBIS instance, run the following command from your command line:
```shell
java -jar app.jar <openBIS URL> <username> <password> dump --ioFile <output file>
```
where `<openBIS URL>` is the URL of your openBIS instance, `<username>` is the username, `<password>` the password and `<output file>` is the path of a JSON file 
where the metadata will be dumped to.

### Import Schema
To import the schema, run
```shell
java -jar app.jar <openBIS URL> <username> <password> load --ioFile <input file>
```
where all arguments are the same and `<input file>` is the JSON file with the desired master data structure.

### Schema Definition with JSON
The metadata of an instance is defined hierarchically using a single JSON file.  
This file contains the following sections (indentation indicates nesting):
- Spaces
  - Projects
    - Collections
- Property types
- Object types

The file should have the following structure:

```JSON
{
  "spaces": [
    {
      "code": "YOUR_SPACE_CODE",
      "description": "Your Description",
      "projects": [
        {
          "code": "YOUR_FIRST_PROJECT_CODE",
          "description": "Your project in the space",
          "collections": [
            {
              "code": "YOUR_FIRST_COLLECTION_CODE",
              "description": "This is an example collection",
              "type": "COLLECTION"
            }
          ]
        }
      ]
    }
  ]
}
```
#### Hierarchical Structure
Consider the `spaces` key in the JSON above. Here we define all our spaces as a list of JSON object. 
Each space object must have a `code` and a `description`. All projects belonging to this space go into the `projects` list of JSON objects.
The member of this list have the following attributes: `code`, `description` and `collections`. In the `collections` attribute, you can define collections belonging to this project. 
This is done in the same way as for the projects, but additionally you must provide a `type` attribute, which should be the  **valid** name of a collection type.

#### Property Types
The second key of the JSON is `property_types`

## Limitations
Currently, the tool is limited in its abilities as it is still under development. If you particularly miss a certain functionality, feel free to open an issue or to contact us.  
### Supported Entities
Only the following openBIS entities/structures are supported for creation and export:
- Spaces
- Projects
- Collections
- Object types
- Property types
### Authentication 
The tool only supports authentication of openBIS users that are registered with file-based or LDAP-based authentication. It does not work with users whose authentication is handled by Switch AAI / eduID.

## Architecture

### Motivation
This section documents the architecture of the system  and explains the main decisions behind the its design. It follows the arc42 template for software architecture documentation.

### Introduction and Goals
The goal of this tool is to offer "power" users an easy way to import and export the schema of openBIS instances in a machine- and human-readable format. 
The primary user for this is the openBIS app developer, who needs to set up a  test instance with a certain schema and wants to automate this process as part of their CI/CD pipeline.

#### Requirements Overview
The system is inspired by the current "master data import" function of openBIS, which uses XLSX files instead and only works for importing master data. 
This tool complements this feature by offering more programmer-friendly features.

The following functional requirements should be covered by this system:
- Export the schema ("master data") from an existing openBIS instance in a convenient format (JSON, YML)
- Import the schema written as JSON or YML in a new openBIS instance
- Validate the schema file 
#### Quality Goals
- Invalid schemas will be detected and a meaningful message displayed.
- Importing in a new instance is transactional: if anything fails during the process, the  openBIS instance state is left unchanged.
- The tool should be extensible: it must be easy to add new openBIS entity types to the serialisation-deserialisation process
#### Stakeholder
To be defined yet.

### Constraints
The tool shall be:
- portable: it should run on all system with a modern JRE
- released with an open source license
- integrate seamlessly with CI-CD pipelines, hence it should offer a command line interface
- Be built and released using the gradle build tool and the gitlab CI/CD pipeline

### Context and Scope
#### Business Context
The system interacts with openBIS as well as with the local filesystem. The interactions with openBIS are needed to create and retreive entities, the interactions with the filesystem to persist and retreive the entities in the configuration file.
```puml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

Person(user, "User")
System(initialiser, "InstanceIO")

System(openbis, "openBIS")
System(filesystem, "File System")
Rel(user, openbis, "uses")
Rel(user, initialiser, "uses")
Rel(initialiser, openbis, "Creates entities")
Rel(openbis, initialiser, "Reads entities")
Rel(initialiser, filesystem, "Writes configuration")
Rel(filesystem, initialiser, "Reads configuration")
@enduml
```

#### Technical Context
Currently left blank

### Solution Strategy
- 