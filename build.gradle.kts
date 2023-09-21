plugins {
    `kotlin-dsl`
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = "com.jamesward.kotlin-universe-catalog"

version = "2023.09.21"

gradlePlugin {
    plugins {
        create("KotlinUniverseCatalog") {
            id = "com.jamesward.kotlin-universe-catalog"
            implementationClass = "com.jamesward.kotlinuniversecatalog.GradlePlugin"
        }
    }
}

kotlin {
    jvmToolchain(8)
}
