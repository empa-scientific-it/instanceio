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
   sdk install gradle 7.6
   ```
4. Install Kotlin:  
    ```shell
   sdk install kotlin
   ```
### Build and run
#### Using the Kotlin Application Plugin
To build the project, use gradle with the `installDir` task. This will create a local installation:
```shell
gradle installDir
```
The installation is in ` app/build/install/app/bin/app`

#### Building a fatJar
If you prefer, you can use the `buildFatJar` gradle task to build a *fat Jar*, this is a java archive which bundles all dependencies in one file that you can deploy anywhere.
To do so, use
```shell
gradle buildFatJar
```




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