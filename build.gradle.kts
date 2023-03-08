import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"

    id("io.papermc.paperweight.userdev") version "1.5.2"
    id("xyz.jpenilla.run-paper") version "2.0.1" // Adds runServer and runMojangMappedServer tasks for testing
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1" // Generates plugin.yml
}


val javadoc by tasks.existing(Javadoc::class)
val jar by tasks.existing

group = "github.rainbowmori"
version = "1.0.0"
description = "Rainbow API"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
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

repositories {
    mavenCentral()
    maven(url = "https://libraries.minecraft.net")
    maven(url = "https://jitpack.io")
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.mojang:brigadier:1.0.18")
    implementation("dev.jorel:commandapi-shade:8.7.0")
    implementation("io.papermc:paperlib:1.0.7")
    implementation("io.github.skytasul:glowingentities:1.1.3")
}

val subTestCreateDirectory = "C:\\Users\\moriy\\Desktop\\java\\OfroPluginCreateServer\\plugins"
val testCreateDirectory = "C:\\Users\\moriy\\Desktop\\java\\1.19.3_TestServer\\plugins"

val buildFileName = "${rootProject.name}-${project.version}.jar"

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

    shadowJar {
        dependencies {
            include(dependency("dev.jorel:commandapi-shade:8.7.0"))
            include(dependency("io.papermc:paperlib:1.0.7"))
            include(dependency("io.github.skytasul:glowingentities:1.1.3"))
        }

        relocate("dev.jorel.commandapi", "github.rainbowmori.rainbowapi.object.commandapi")
        relocate("io.papermc.lib", "github.rainbowmori.rainbowapi.object.paperlib")
        relocate("fr.skytasul.glowingentities", "github.rainbowmori.rainbowapi.object.glowingentities")
    }

//    val sourcesJarCreate by creating(Jar::class) {
//        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
//        archiveClassifier.set("sources")
//        from(sourceSets["main"].allSource)
//    }

//    val javadocJarCreate by creating(Jar::class) {
//        dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
//        archiveClassifier.set("javadoc")
//        from(javadoc)
//    }

    val testCopied = register("testCopied", Copy::class.java){
        from(layout.buildDirectory.file("libs/${buildFileName}"))
        into(file(subTestCreateDirectory))
    }

    val copied = register("copied", Copy::class.java) {
        from(layout.buildDirectory.file("libs/${buildFileName}"))
        into(file(testCreateDirectory))
    }
    reobfJar{
//        dependsOn(javadocJarCreate)
//        dependsOn(sourcesJarCreate)
        finalizedBy(copied)
        finalizedBy(testCopied)
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