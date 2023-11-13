// needed to handle bump then build
pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/groups/staging")  // since mavenCentral takes a little while
    }
}

// needed to handle bump then build
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/staging")  // since mavenCentral takes a little while
    }
}

plugins {
    id("com.jamesward.kotlin-universe-catalog") version "2023.11.13-3"
}

include("stables", "unstables", "gradle-plugin")