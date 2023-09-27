pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/groups/staging")  // since mavenCentral takes a little while
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

plugins {
    id("com.jamesward.kotlin-universe-catalog") version "2023.09.26-4"
}
