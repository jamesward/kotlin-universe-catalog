plugins {
    `version-catalog`
    `maven-publish`
}

group = "com.jamesward.kotlin-universe-catalog"

catalog {
    versionCatalog {
        from(files("gradle/libs.versions.toml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
