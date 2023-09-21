plugins {
    `kotlin-dsl`
    `maven-publish`
    signing
    id("com.gradle.plugin-publish") version "1.2.1"
    id("me.qoomon.git-versioning") version "6.4.2"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
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

val pluginName = "Kotlin Universe Catalog"
val pluginDescription = "Gradle convention plugin that defines version catalogs for the universe of Kotlin Gradle plugins and libraries"
val pluginUrl = "https://github.com/jamesward/kotlin-universe-catalog"
val pluginScm = "scm:git:git@github.com:jamesward/kotlin-universe-catalog.git"

@Suppress("UnstableApiUsage")
gradlePlugin {
    website = pluginUrl
    vcsUrl = pluginUrl
    plugins {
        create("KotlinUniverseCatalog") {
            id = "com.jamesward.kotlin-universe-catalog"
            implementationClass = "com.jamesward.kotlinuniversecatalog.GradlePlugin"
            displayName = pluginName
            description = pluginDescription
            tags = listOf("kotlin")
        }
    }
}

kotlin {
    jvmToolchain(8)
}

@Suppress("UnstableApiUsage")
publishing {
    publications {
        configureEach {
            (this as MavenPublication).pom {
                name = pluginName
                description = pluginDescription
                url = pluginUrl

                scm {
                    connection = "scm:git:$pluginUrl.git"
                    developerConnection = pluginScm
                    url = pluginUrl
                }

                licenses {
                    license {
                        name = "Apache 2.0"
                        url = "https://opensource.org/licenses/Apache-2.0"
                    }
                }

                developers {
                    developer {
                        id = "jamesward"
                        name = "James Ward"
                        email = "james@jamesward.com"
                        url = "https://jamesward.com"
                    }
                }
            }
        }
    }
}

@Suppress("UnstableApiUsage")
nexusPublishing.repositories {
    sonatype {
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
    }
}

signing {
    isRequired = System.getenv("GPG_PRIVATE_KEY") != null && System.getenv("GPG_PASSPHRASE") != null
    useInMemoryPgpKeys(System.getenv("GPG_PRIVATE_KEY"), System.getenv("GPG_PASSPHRASE"))
}
