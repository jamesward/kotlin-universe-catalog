import me.qoomon.gitversioning.commons.GitUtil
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

plugins {
    alias(universe.plugins.qoomon.git.versioning)
    alias(universe.plugins.gradle.nexus.publish.plugin)
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

subprojects {
    apply {
        plugin("maven-publish")
        plugin("signing")
    }

    @Suppress("UnstableApiUsage")
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

    //extensions.getByType<SigningExtension>().sign(extensions.getByType<PublishingExtension>().publications.named("maven").get())
    extensions.getByType<SigningExtension>().useInMemoryPgpKeys(System.getenv("GPG_PRIVATE_KEY"), System.getenv("GPG_PASSPHRASE"))

    tasks.withType<Sign> {
        onlyIf { System.getenv("GPG_PRIVATE_KEY") != null && System.getenv("GPG_PASSPHRASE") != null }
    }
}

@Suppress("UnstableApiUsage")
nexusPublishing.repositories {
    sonatype {
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
    }
}
