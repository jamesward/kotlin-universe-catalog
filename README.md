# Kotlin Universe Catalog

Usage:

`settings.gradle.kts`
```
plugins {
    id("com.jamesward.kotlin-universe-catalog") version "2023.09.27-1"
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
- Channels (stable, rc, beta, alpha)
  - Using an RC can result in an older version than stable
  - How will dependabot bump? stable always stable, rcs always rcs, etc?
