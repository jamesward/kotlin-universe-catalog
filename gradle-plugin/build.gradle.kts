import com.vanniktech.maven.publish.JavadocJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    `kotlin-dsl`
    alias(universe.plugins.gradle.plugin.publish)
    id("com.vanniktech.maven.publish")
}

group = "com.jamesward.kotlin-universe-catalog"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    testImplementation(gradleTestKit())
    testImplementation(universe.junit.jupiter)
    testRuntimeOnly(universe.junit.platform.launcher)
}

mavenPublishing {
    configure(com.vanniktech.maven.publish.GradlePublishPlugin())
}

publishing {
    repositories {
        maven {
            name = "test"
            url = uri(rootProject.layout.buildDirectory.dir("maven-repo"))
        }
    }
}

tasks.named<Test>("test") {
    dependsOn("publishAllPublicationsToTestRepository")
    dependsOn(":stables:publishAllPublicationsToTestRepository")
    dependsOn(":unstables:publishAllPublicationsToTestRepository")

    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
        events(STARTED, PASSED, SKIPPED, FAILED)
    }

    systemProperties["test-repo"] = uri(rootProject.layout.buildDirectory.dir("maven-repo"))
    systemProperties["plugin-version"] = project.version
}


val generatedResourcesDir = layout.buildDirectory.dir("generated-resources")
val writeprops = tasks.create<WriteProperties>("writeprops") {
    destinationFile.set(generatedResourcesDir.map { it.file("META-INF/kotlin-universe-catalog.properties") })
    property("version", project.version)
}

sourceSets {
    main {
        resources {
            srcDir {
                files(generatedResourcesDir) {
                    builtBy(writeprops)
                }
            }
        }
    }
}

@Suppress("UnstableApiUsage")
gradlePlugin {
    website = rootProject.ext["pluginUrl"].toString()
    vcsUrl = rootProject.ext["pluginUrl"].toString()
    plugins {
        create("KotlinUniverseCatalog") {
            id = "com.jamesward.kotlin-universe-catalog"
            implementationClass = "com.jamesward.kotlinuniversecatalog.GradlePlugin"
            displayName = rootProject.ext["pluginName"].toString()
            description = rootProject.ext["pluginDescription"].toString()
            tags = listOf("kotlin")
        }
    }
}
