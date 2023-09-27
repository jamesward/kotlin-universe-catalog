import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    `kotlin-dsl`
    alias(universe.plugins.gradle.plugin.publish)
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
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
        events(STARTED, PASSED, SKIPPED, FAILED)
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
