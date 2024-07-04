# Kotlin Universe Catalog

Common dependencies in convenient catalogs so you can autocomplete your way to typed dependency bliss.
- Based on Gradle Version Catalogs
- One version that updates all your deps to the latest

Two Catalogs:
 - *stable* ([view catalog](stables/gradle/libs.versions.toml)) - Follows the latest stable releases
 - *unstable* - ([view catalog](unstables/gradle/libs.versions.toml)) - Follows the latest unstable releases (alphas, betas, rcs)

Add the settings plugin to your Gradle `settings.gradle.kts` file:
```
plugins {
    id("com.jamesward.kotlin-universe-catalog") version "2024.07.03-2"
}
```

Now in your Gradle build files, use version catalog references, like:
```
plugins {
    alias(universe.plugins.kotlin.jvm)
}

dependencies {
    implementation(universe.kotlinx.coroutines.core)
}
```

You can also use the unstable universe which includes alphas, betas, and rcs with `universeunstable`.

If you'd rather not use the settings plugin you can depend directly on the Version Catalog artifacts:

`settings.gradle.kts`
```
dependencyResolutionManagement {
    versionCatalogs {
        create("universe") {
            from("com.jamesward.kotlin-universe-catalog:stables:2024.07.03-2")
        }
    }
}
```

TODO:
- Compatibility Matrix (Library X needs plugin Y needs Kotlin Z)
- BOMs
  - Validate that when a bom is used, the resolution uses it instead of the catalog version
