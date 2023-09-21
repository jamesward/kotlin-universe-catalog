pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/groups/staging") // since mavenCentral takes a little while
    }
}

plugins {
    id("com.jamesward.kotlin-universe-catalog") version "2023.09.21-a6dddc0"
}
