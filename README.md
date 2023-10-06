# Kotlin Universe Catalog

Usage:

`settings.gradle.kts`
```
plugins {
    id("com.jamesward.kotlin-universe-catalog") version "2023.10.06-2"
}
```

`build.gradle.kts`
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
            from("com.jamesward.kotlin-universe-catalog:stables:2023.10.06-2")
        }
    }
}
```

TODO:
- Compatibility Matrix (Library X needs plugin Y needs Kotlin Z)
