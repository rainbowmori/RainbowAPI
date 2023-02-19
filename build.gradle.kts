import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    `maven-publish`

    id("io.papermc.paperweight.userdev") version "1.5.0"
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "github.rainbowmori"
version = "1.0.0"
description = "Rainbow API"

repositories {
    mavenCentral()
    maven(url = "https://libraries.minecraft.net")
    maven(url = "https://jitpack.io")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            group = "github.rainbowmori"
            version = "1.0.0"
            artifactId = "RainbowAPI"
            from(components["java"])
        }
    }
}


dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.mojang:brigadier:1.0.18")
}


java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}




bukkit {
    // Default values can be overridden if needed
    // name = 'TestPlugin'
    // version = '1.0'
    // description = 'This is a test plugin'
    // website = 'https://example.com'
    // author = 'Notch'

    // Plugin main class (required)
    main = "github.rainbowmori.rainbowapi.RMHome"

    // API version (should be set for 1.13+)
    apiVersion = "1.19"

    authors = listOf("rainbowmori")
    prefix = "RainbowAPI"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP

//    commands {
//        test {
//            description = 'This is a test command!'
//            aliases = ['t']
//            permission = 'testplugin.test'
//            usage = 'Just run the command!'
//            // permissionMessage = 'You may not test this command!'
//        }
//        // ...
//    }

//    permissions {
//        'testplugin.*' {
//            children = ['testplugin.test'] // Defaults permissions to true
//            // You can also specify the values of the permissions
//            childrenMap = ['testplugin.test': false]
//        }
//        'testplugin.test' {
//            description = 'Allows you to run the test command'
//            setDefault('OP') // 'TRUE', 'FALSE', 'OP' or 'NOT_OP'
//        }
//    }
}