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
### Build
To build the project, use gradle:
```shell
gradle build
```

## How to use the tool
Once you have built the project, you can use the tool to both import and export metadata (and data!) from an existing openBIS instance.
