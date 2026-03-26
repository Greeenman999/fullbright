/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 rotgruengelb, and stonecutter-mod-template contributors
 * Copyright (c) 2025 murder_spagurder
 * Copyright (c) 2026 Greenman999
 * See the LICENSE file in the project root for license terms.
 */

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://jitpack.io") { name = "Jitpack" }
        exclusiveContent {
            forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
            filter { includeGroup("maven.modrinth") }
        }
    }
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9"
}

stonecutter {
    create(rootProject) {
        fun match(version: String, vararg loaders: String) =
            loaders.forEach { version("$version-$it", version).buildscript = "build.$it.gradle.kts" }

//        match("1.19.2", "fabric", "forge")
//        match("1.19.3", "fabric", "forge")
//        match("1.19.4", "fabric", "forge")
//        match("1.20.1", "fabric", "forge")
//        match("1.20.2", "fabric", "neoforge")
//        match("1.20.4", "fabric", "neoforge")
//        match("1.20.6", "fabric", "neoforge")
//        match("1.21.1", "fabric", "neoforge")
//        match("1.21.3", "fabric", "neoforge")
//        match("1.21.4", "fabric", "neoforge")
//        match("1.21.5", "fabric", "neoforge")
//        match("1.21.8", "fabric", "neoforge")
//        match("1.21.10", "fabric", "neoforge")
        match("1.21.11", "fabric", "neoforge")
        match("26.1", "fabric")


        vcsVersion = "1.21.11-fabric"
    }
}