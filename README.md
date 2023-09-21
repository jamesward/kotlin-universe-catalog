# Kotlin Universe Catalog

Usage:

`settings.gradle.kts`
```
plugins {
    id("com.jamesward.kotlin-universe-catalog") version "2023.09.21-de4b5ff"
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


TODO:
- Automatic updating from dependabot? Or Maven Central & Google Maven?
- Tests ?
- Channels (stable, beta, alpha)
