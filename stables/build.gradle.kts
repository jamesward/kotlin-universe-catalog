import org.tomlj.Toml
import org.tomlj.TomlParseResult
import java.util.*

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.tomlj:tomlj:1.1.0")
    }
}

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

tasks.create("test") {
    doLast {
        val tomlParseResult = Toml.parse(file("gradle/libs.versions.toml").toPath())
        if (tomlParseResult.hasErrors()) {
            tomlParseResult.errors().forEach {
                println(it)
            }
            throw AssertionError("Error Parsing toml")
        }
        else {
            // no duplicate identifiers

            // no duplicate group & name

            // no unstables

            // resolve deps? - what about deps from bom versions

        }
    }
}

data class Plugin(val key: String, val id: String, val version: String) {
    override fun toString(): String =
        """$key = { id = "$id", version = "$version" }"""

    override fun equals(other: Any?): Boolean =
        (other as? Plugin)?.let {
            id == it.id
        } ?: false

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

data class Library(val key: String, val group: String, val name: String, val version: String?) {
    override fun toString(): String =
        if (version != null) {
            """$key = { group = "$group", name = "$name", version = "$version" }"""
        }
        else {
            """$key = { group = "$group", name = "$name" }"""
        }

    override fun equals(other: Any?): Boolean =
        (other as? Library)?.let {
            group == other.group && name == other.name
        } ?: false

    override fun hashCode(): Int {
        var result = group.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

}

fun parseToml(tomlParseResult: TomlParseResult): Triple<Map<String, String>, Set<Plugin>, Set<Library>> {
    if (tomlParseResult.hasErrors()) {
        tomlParseResult.errors().forEach {
            println(it)
        }
        throw AssertionError("Error Parsing toml")
    }
    else {
        val versions = tomlParseResult.getTable("versions")
        val libraries = tomlParseResult.getTable("libraries")
        val plugins = tomlParseResult.getTable("plugins")

        val versionsParsed: Map<String, String> = versions?.toMap()?.mapValues {
            it.value as String
        } ?: emptyMap()

        val pluginsParsed: Set<Plugin> = plugins?.entrySet()?.map { plugin ->
            val pluginTable = plugin.value as org.tomlj.TomlTable
            val versionMaybe = if (pluginTable.dottedKeySet().contains("version.ref")) {
                val versionRef = pluginTable.dottedEntrySet().find { it.key == "version.ref" }
                (versionRef?.value as String).let {
                    versions?.getString(it)
                }
            } else {
                pluginTable.entrySet().find { it.key == "version" }?.value as? String
            }

            val idMaybe = pluginTable.entrySet().find { it.key == "id" }?.value as? String

            idMaybe?.let { id ->
                versionMaybe?.let { version ->
                    Plugin(plugin.key, id, version)
                }
            }
        }?.filterNotNull()?.toSet() ?: emptySet()

        val librariesParsed: Set<Library> = libraries?.entrySet()?.map { plugin ->
            val pluginTable = plugin.value as org.tomlj.TomlTable
            val version: String? = if (pluginTable.dottedKeySet().contains("version.ref")) {
                val versionRef = pluginTable.dottedEntrySet().find { it.key == "version.ref" }
                (versionRef?.value as String).let {
                    versions?.getString(it)
                }
            } else {
                pluginTable.entrySet().find { it.key == "version" }?.value as? String
            }

            val groupAndName: Pair<String, String>? = if (pluginTable.keySet().contains("module")) {
                val module = pluginTable.entrySet().find { it.key == "module" }?.value as? String
                val moduleList = module?.split(':')
                if (moduleList?.size == 2) {
                    moduleList[0] to moduleList[1]
                } else {
                    null
                }
            } else {
                val groupMaybe = pluginTable.entrySet().find { it.key == "group" }?.value as? String
                val nameMaybe = pluginTable.entrySet().find { it.key == "name" }?.value as? String
                groupMaybe?.let { group ->
                    nameMaybe?.let { name ->
                        group to name
                    }
                }
            }

            groupAndName?.let {
                Library(plugin.key, groupAndName.first, groupAndName.second, version)
            }
        }?.filterNotNull()?.toSet() ?: emptySet()

        return Triple(versionsParsed, pluginsParsed, librariesParsed)
    }
}

tasks.create("merge") {
    doLast {
        val libsFile = file("gradle/libs.versions.toml")

        println("Paste some toml and hit Ctrl-D when ready for parsing:")

        val existing = parseToml(Toml.parse(libsFile.toPath()))
        val incoming = parseToml(Toml.parse(System.`in`))

        val updatedPlugins = existing.second + incoming.second
        val updatedLibraries = existing.third + incoming.third

        val lines = mutableListOf<String>()
        lines += "[versions]"
        existing.first.forEach {
            lines += """${it.key} = "${it.value}""""
        }
        lines += ""

        lines += "[plugins]"
        updatedPlugins.sortedBy { it.key + " " + it.id }.forEach {
            lines += it.toString()
        }
        lines += ""

        lines += "[libraries]"
        updatedLibraries.sortedBy { it.key + " " + it.group + " " + it.name }.forEach {
            lines += it.toString()
        }

        libsFile.writeText(lines.joinToString("\n"))
    }
}

tasks.create("canonical") {
    doLast {
        println("Paste some toml and hit Ctrl-D when ready for parsing:")

        //val tomlParseResult = Toml.parse(System.`in`)
        val tomlParseResult = Toml.parse(file("/tmp/libs.versions.toml").toPath())
        println(parseToml(tomlParseResult))
    }
}