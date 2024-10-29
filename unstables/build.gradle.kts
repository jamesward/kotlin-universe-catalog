plugins {
    `version-catalog`
    id("com.vanniktech.maven.publish")
}

group = "com.jamesward.kotlin-universe-catalog"

catalog {
    versionCatalog {
        from(files("gradle/libs.versions.toml"))
    }
}

mavenPublishing {
    configure(com.vanniktech.maven.publish.VersionCatalog())
}

publishing {
    repositories {
        maven {
            name = "test"
            url = uri(rootProject.layout.buildDirectory.dir("maven-repo"))
        }
    }
}
