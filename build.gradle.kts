import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    `maven-publish`

    id("io.papermc.paperweight.userdev") version "1.5.2"
    id("xyz.jpenilla.run-paper") version "2.0.1" // Adds runServer and runMojangMappedServer tasks for testing
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1" // Generates plugin.yml
}


val javadoc by tasks.existing(Javadoc::class)
val jar by tasks.existing

group = "github.rainbowmori"
version = "1.0.0"
description = "Rainbow API"

repositories {
    mavenCentral()
    maven(url = "https://libraries.minecraft.net")
    maven(url = "https://jitpack.io")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                artifact(tasks.reobfJar) {
                    classifier = ""
                }
            }
        }
    }
}

dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.mojang:brigadier:1.0.18")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
        isFailOnError = false
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

bukkit {
    name = "RainbowAPI"
    main = "github.rainbowmori.rainbowapi.RMHome"
    version = "${project.version}"
    apiVersion = "1.19"
    prefix = "RainbowAPI"
    authors = listOf("rainbowmori")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
}