plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.1"
    id("me.qoomon.git-versioning") version "6.4.2"
}

repositories {
    mavenCentral()
}

group = "com.jamesward.kotlin-universe-catalog"

gitVersioning.apply {
    rev {
        version = "\${commit.timestamp.year}.\${commit.timestamp.month}.\${commit.timestamp.day}-\${commit.short}"
    }
}

@Suppress("UnstableApiUsage")
gradlePlugin {
    website = "https://github.com/jamesward/kotlin-universe-catalog"
    vcsUrl = "https://github.com/jamesward/kotlin-universe-catalog"
    plugins {
        create("KotlinUniverseCatalog") {
            id = "com.jamesward.kotlin-universe-catalog"
            implementationClass = "com.jamesward.kotlinuniversecatalog.GradlePlugin"
            displayName = "Kotlin Universe Catalog"
            description = "Gradle convention plugin that defines version catalogs for the universe of Kotlin Gradle plugins and libraries"
            tags = listOf("kotlin")
        }
    }
}

kotlin {
    jvmToolchain(8)
}
