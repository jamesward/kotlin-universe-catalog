import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import me.qoomon.gitversioning.commons.GitUtil
import nl.littlerobots.vcu.plugin.resolver.VersionSelectors
import nl.littlerobots.vcu.plugin.versionSelector
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

plugins {
    // https://github.com/vanniktech/gradle-maven-publish-plugin/issues/812
    `kotlin-dsl` apply false

    alias(universe.plugins.qoomon.git.versioning)

    // causes an issue with gradle-plugin using kotlin-dsl
    //alias(universe.plugins.kotlin.power.assert)

    // temp use non-catalog
    id("com.github.ben-manes.versions") version "0.41.0"
    id("nl.littlerobots.version-catalog-update") version "0.8.5"
    id("com.vanniktech.maven.publish") version "0.30.0" apply false
}

group = "com.jamesward.kotlin-universe-catalog"

gitVersioning.apply {
    val repository = FileRepositoryBuilder().findGitDir(project.projectDir).build()
    val tags = GitUtil.tags(repository)
    val head = GitUtil.worktreesFix_resolveHead(repository)
    val headTimestamp = GitUtil.revTimestamp(repository, head)
    val tagsForTimestamp = tags.filter {
        val timestamp = GitUtil.revTimestamp(repository, it.objectId)
        timestamp.year == headTimestamp.year &&
                timestamp.month == headTimestamp.month &&
                timestamp.dayOfMonth == headTimestamp.dayOfMonth
    }

    rev {
        version = "\${commit.timestamp.year}.\${commit.timestamp.month}.\${commit.timestamp.day}-${tagsForTimestamp.size + 1}"
    }
}

ext["pluginName"] = "Kotlin Universe Catalog"
ext["pluginDescription"] = "Gradle convention plugin that defines version catalogs for the universe of Kotlin Gradle plugins and libraries"
ext["pluginUrl"] = "https://github.com/jamesward/kotlin-universe-catalog"

/*
configure<com.bnorm.power.PowerAssertGradleExtension> {
    functions = listOf("kotlin.test.assertTrue")
}
 */

versionCatalogUpdate {
    keep {
        keepUnusedVersions = true
    }

    versionSelector {
        if (it.candidate.group == "com.google.errorprone" &&
            it.candidate.module == "javac" &&
            !it.candidate.version.contains("-dev")) {
            true
        } else {
            // to prevent something like FINAL-SNAPSHOT as a version
            !it.candidate.version.contains("-SNAPSHOT") && VersionSelectors.STABLE.select(it)
        }
    }

    versionCatalogs {
        create("stables") {
            catalogFile = file("stables/gradle/libs.versions.toml")
        }
        create("unstables") {
            catalogFile = file("unstables/gradle/libs.versions.toml")
            versionSelector {
                // whatever is latest, except for snapshots
                !it.candidate.version.endsWith("-SNAPSHOT")
            }
        }
    }
}

subprojects {
    apply {
        plugin("com.vanniktech.maven.publish")
    }

    extensions.getByType<MavenPublishBaseExtension>().apply {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

        if (System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey") != null && System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword") != null) {
            signAllPublications()
        }

        pom {
            name = rootProject.ext["pluginName"].toString()
            description = rootProject.ext["pluginDescription"].toString()
            url = rootProject.ext["pluginUrl"].toString()

            scm {
                connection = "scm:git:${rootProject.ext["pluginUrl"]}.git"
                developerConnection = "scm:git:git@github.com:jamesward/kotlin-universe-catalog.git"
                url = rootProject.ext["pluginUrl"].toString()
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
