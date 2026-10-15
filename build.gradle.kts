plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.17.21" apply false
    id("net.fabricmc.fabric-loom") version "1.17.21" apply false
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    kotlin("jvm") version "2.3.20"
}

val unobfuscated = stonecutter.eval(stonecutter.current.version, ">=26.1")
apply(plugin = if (unobfuscated) "net.fabricmc.fabric-loom" else "net.fabricmc.fabric-loom-remap")
val loom = extensions.getByType<net.fabricmc.loom.api.LoomGradleExtensionAPI>()
val modImplementation = if (unobfuscated) "implementation" else "modImplementation"
java.withSourcesJar()
val modJar = tasks.named<AbstractArchiveTask>(if (unobfuscated) "jar" else "remapJar")
val modSourcesJar = tasks.named<AbstractArchiveTask>(if (unobfuscated) "sourcesJar" else "remapSourcesJar")

val requiredJava = when {
    unobfuscated -> JavaVersion.VERSION_25
    stonecutter.eval(stonecutter.current.version, ">=1.20.6") -> JavaVersion.VERSION_21
    stonecutter.eval(stonecutter.current.version, ">=1.18") -> JavaVersion.VERSION_17
    stonecutter.eval(stonecutter.current.version, ">=1.17") -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

version = "${property("mod.version")}+${stonecutter.current.version}"
base.archivesName = property("mod.id") as String

repositories {
	mavenCentral()
	maven("https://maven.fabricmc.net/")
    maven("https://maven.terraformersmc.com/")
	maven("https://maven.bawnorton.com/releases")
    maven("https://repo.essential.gg/repository/maven-public")
}

dependencies {
    add("minecraft", "com.mojang:minecraft:${stonecutter.current.project}")
    if (!unobfuscated) add("mappings", loom.officialMojangMappings())
    add(modImplementation, "net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    add(modImplementation, "net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
    add(modImplementation, "net.fabricmc:fabric-language-kotlin:1.13.10+kotlin.2.3.20")

    implementation("gg.essential:elementa:${property("deps.elementa")}")
    add("include", "gg.essential:elementa:${property("deps.elementa")}")
    add(modImplementation, "gg.essential:universalcraft-${property("deps.elementa.mc")}-fabric:${property("deps.universalcraft")}")
    add("include", "gg.essential:universalcraft-${property("deps.elementa.mc")}-fabric:${property("deps.universalcraft")}")

    add(modImplementation, "com.terraformersmc:modmenu:${property("deps.modmenu")}")
}

stonecutter {
    replacements.string(current.parsed >= "1.21.11") {
        replace("ResourceLocation", "Identifier")
    }
    replacements.string(current.parsed >= "26.1") {
        replace("ClientCommandManager", "ClientCommands")
        replace("KeyBindingHelper", "KeyMappingHelper")
        replace("client.keybinding.v1", "client.keymapping.v1")
        replace("registerKeyBinding", "registerKeyMapping")
    }
    replacements.string(current.parsed >= "26.3") {
        replace("InputConstants.Type.KEYSYM", "InputConstants.Type.KEYBOARD")
    }
}

tasks {
    processResources {
        inputs.property("id", project.property("mod.id"))
        inputs.property("name", project.property("mod.name"))
        inputs.property("version", project.property("mod.version"))
        inputs.property("minecraft", project.property("mod.mc_dep"))
        inputs.property("loader", project.property("deps.fabric_loader"))
        val fabricModJsonJava = ">=${requiredJava.majorVersion}"

        val props = mapOf(
            "id" to project.property("mod.id"),
            "name" to project.property("mod.name"),
            "version" to project.property("mod.version"),
            "minecraft" to project.property("mod.mc_dep"),
            "loader" to if (unobfuscated) project.property("deps.fabric_loader") else "0.17.2",
            "java" to fabricModJsonJava
        )
        filesMatching("fabric.mod.json") {
            expand(props)
        }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.base.archivesName.get()}" }
        }
    }

    // Builds the version into a shared folder in `build/libs/${mod version}/`
    register<Copy>("buildAndCollect") {
        group = "build"
        from(modJar.map { it.archiveFile }, modSourcesJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

loom.apply {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json") // Useful for interface injection
    //accessWidenerPath = rootProject.file("src/main/resources/fullbright.accesswidener")

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
    }

    runConfigs.all {
        ideConfigGenerated(true) // Run configurations are not created for subprojects by default
        vmArgs("-Dmixin.debug.export=true") // Exports transformed classes for debugging
        runDir = "../../run" // Use a shared run folder and create separate worlds
    }
}

java {
	sourceCompatibility = requiredJava
	targetCompatibility = requiredJava
}

kotlin {
    jvmToolchain(requiredJava.majorVersion.toInt())
}

// Publishes builds to Modrinth and Curseforge with changelog from the CHANGELOG.md file
publishMods {
    file = modJar.map { it.archiveFile.get() }
    additionalFiles.from(modSourcesJar.map { it.archiveFile.get() })
    displayName = "${property("mod.name")} ${property("mod.version")} for ${property("mod.mc_title")}"
    version = property("mod.version") as String
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("fabric")

    dryRun = providers.gradleProperty("MODRINTH_TOKEN").getOrNull() == null

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = providers.gradleProperty("MODRINTH_TOKEN")
        minecraftVersions.addAll(property("mod.mc_targets").toString().split(' '))
        requires {
            slug = "fabric-api"
        }
        requires {
            slug = "fabric-language-kotlin"
        }
        optional {
            slug = "modmenu"
        }
    }
}