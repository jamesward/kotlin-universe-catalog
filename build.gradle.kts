import me.qoomon.gitversioning.commons.GitUtil
import nl.littlerobots.vcu.plugin.resolver.VersionSelectors
import nl.littlerobots.vcu.plugin.versionSelector
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

plugins {
    alias(universe.plugins.qoomon.git.versioning)
    alias(universe.plugins.gradle.nexus.publish.plugin)
    // causes an issue with gradle-plugin using kotlin-dsl
    //alias(universe.plugins.kotlin.power.assert)
    alias(universeunstable.plugins.version.catalog.update)
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

subprojects {
    apply {
        plugin("maven-publish")
        plugin("signing")
    }

    extensions.getByType<PublishingExtension>().publications {
        configureEach {
            (this as MavenPublication).pom {
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

    extensions.getByType<PublishingExtension>().repositories {
        maven {
            url = uri(rootProject.layout.buildDirectory.dir("maven-repo"))
        }
    }

    extensions.getByType<SigningExtension>().useInMemoryPgpKeys(System.getenv("GPG_PRIVATE_KEY"), System.getenv("GPG_PASSPHRASE"))

    tasks.withType<Sign> {
        onlyIf { System.getenv("GPG_PRIVATE_KEY") != null && System.getenv("GPG_PASSPHRASE") != null }
    }
}

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

nexusPublishing.repositories {
    sonatype {
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
    }
}
