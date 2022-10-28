/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.9.3/userguide/building_java_projects.html
 */

plugins {
    val kotlinVer = "1.7.20"
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version kotlinVer

    //Download plugin
    id("de.undercouch.download") version "5.3.0"

    //Serialisation
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVer

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

import de.undercouch.gradle.tasks.download.Download


repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
     ivy {
        url =   uri("https://sissource.ethz.ch/openbis/openbis-public/openbis-ivy/-/raw/main/")
        patternLayout{
      
            artifact("[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]")
             ivy("[organisation]/[module]/[revision]/ivy.xml")
        }
       
    }

}

val openbisJar by configurations.creating




dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    //Openbis v3 API
    implementation("openbis:openbis-v3-api:20.10.5-EA")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    //Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")

    //Email validation
    implementation("jakarta.mail:jakarta.mail-api:2.1.0")

}


tasks {

    val downloadOpenbisDistFile by registering(Download::class) {
        overwrite(false)
        src("https://polybox.ethz.ch/index.php/s/TnE9k60mIeA24lD/download")
        dest(File(buildDir, "allOpenBIS.zip"))
    }

    val unzipOpenbisFile by registering(Copy::class){
        dependsOn(downloadOpenbisDistFile)
        from((zipTree(File(buildDir, "allOpenBIS.zip")).matching {
            include("*API-V3*.zip")
            rename("(openBIS-API-V3)(.*)(.zip)","$1$3")
            }).first())
        into(layout.buildDirectory.dir("temp"))
    }

    val unzipOpenbisAPI by registering(Copy::class){
        dependsOn(unzipOpenbisFile)
        from(zipTree(layout.buildDirectory.file("temp/openBIS-API-V3.zip")).matching{
            include("openBIS-API-V3/openBIS-API*batteries*.jar")
            eachFile {
                relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())  
            }
            rename("(openBIS-API-V3-batteries-included)(.*)(.jar)","$1$3")
        }
        )
        into(layout.projectDirectory.dir("libs"))
    }

}

application {
    // Define the main class for the application.
    mainClass.set("openbisio.AppKt")
}
