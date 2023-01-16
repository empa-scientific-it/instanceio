# OpenBIS Instance Initialiser

## What is it
This repository contains an *experimental* tool written in Kotlin which can import/export entire openBIS instances using JSON files.

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
If you prefer, you can use the `buildFatJar` gradle task to build a *fat Jar*, this is is a java archive which bundles all dependencies in one file that you can deploy anywhere.
To do so, use
```shell
gradle buildFatJar
```


## How to use the tool
Once you have built the project, you can use the tool to both import and export metadata (and data!) from an existing openBIS instance.
