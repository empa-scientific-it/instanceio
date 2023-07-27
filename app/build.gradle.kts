/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.9.3/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)


    //alias(libs.plugins.fatJar)

    //Serialisation
    alias(libs.plugins.kotlin.serialization)

    //Kapt
    alias(libs.plugins.kotlin.kapt)


    // Apply the application plugin to add support for building a CLI application in Java.
    application
    java

    id("maven-publish")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    //Ivy for ETH OpenBIS dependnecies
    ivy {
        url = uri("https://sissource.ethz.ch/openbis/openbis-public/openbis-ivy/-/raw/main/")
        patternLayout {

            artifact("[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]")
            ivy("[organisation]/[module]/[revision]/ivy.xml")
        }

    }
    // Gitlab Package registry to publish the library
    maven {
        val gitLabPrivateToken: String? by project
        val ciToken: String? = System.getenv("CI_JOB_TOKEN")
        url = uri("https://gitlab.empa.ch/api/v4/groups/openbis-tools/packages/maven")
        name = "Empa Gitlab"
        credentials(HttpHeaderCredentials::class) {
            name = "Private-Token"
            value = gitLabPrivateToken ?: ciToken
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }


}

application {
    mainClass.set("ch.empa.openbisio.App")
}

dependencies {


    // Align versions of all Kotlin components
    implementation(libs.kotlin.bom)
    // Use the Kotlin JDK  standard library.
    implementation(libs.kotlin.stdlib)

    // Use the Kotlin test library.
    testImplementation(libs.kotlin.test)
    // Use the Kotlin JUnit integration.
    testImplementation(libs.kotlin.test.junit)

    //Openbis v3 API
    implementation(libs.openbis)

    implementation(libs.kotlinx.serialization)

    //Mapping
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    kapt(libs.mapstruct.processor)

    //Command line parsing
    implementation(libs.kotlinx.cli)



    //Testcontainer for integration tests
    testImplementation(libs.testcontainer)
    //Mocking
    testImplementation(libs.mockk)


}




kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))

    }
}





testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnit()
            useKotlinTest()
        }
        val integrationTest by registering(JvmTestSuite::class) {
            sources {
                kotlin {
                    setSrcDirs(listOf("src/integration/kotlin/"))
                }
                java {
                    setSrcDirs(listOf("src/integration/java/"))

                }
                resources {
                    setSrcDirs(listOf("src/integration/resources/"))
                }
            }

            dependencies {
                implementation(project())
            }
        }
    }
}


publishing {
    repositories {
        maven {
            val gitLabPrivateToken: String? by project
            val ciToken: String? = System.getenv("CI_JOB_TOKEN")
            println(gitLabPrivateToken)
            url = uri("https://gitlab.empa.ch/api/v4/projects/1644/packages/maven/")
            name = "EmpaGitlab"
            credentials(HttpHeaderCredentials::class) {
                name = "Private-Token"
                value = gitLabPrivateToken
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}


/* Confiugre integration tests */
val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}


tasks.jar {
    manifest {
        attributes["Main-Class"] = "ch.empa.openbisio.AppKt"
    }
    sourceSets {
        getByName("main") {
            kotlin {
                srcDirs("src/main/kotlin")
            }
        }
    }
    from({ configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) } })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}



