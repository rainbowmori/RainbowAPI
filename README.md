# RainbowAPI

## 自分用に初期化させる方法を書いておく

```
dependencies {
    compileOnly 'io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT'

    implementation files(projectDir.getParentFile().toString() + '\\RainbowAPI\\build\\libs\\RainbowAPI.jar')
}

def buildFileName = "${project.name}.jar"

def multipleDirectory = [projectDir.getParentFile().getParentFile().toString() + '\\ServerName\\plugins']

tasks.register('copyJar'){
    doLast {
        multipleDirectory.each { outputDir ->
            copy {
                from "$buildDir\\libs\\$buildFileName"
                into outputDir
            }
        }
    }
}

jar{
 archiveName(buildFileName)
}

build.finalizedBy copyJar

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0,'seconds')
}

================== plugin.yml ======================

depend : [RainbowAPI]

```

```
repositories {
    maven { url = 'https://jitpack.io' }
}
dependencies {
	implementation 'com.github.rainbowmori:RainbowAPI:main-SNAPSHOT'
}
```

## 使用している repository

- [https://github.com/PaperMC/PaperLib](https://github.com/PaperMC/PaperLib)
- [https://github.com/JorelAli/CommandAPI](https://github.com/JorelAli/CommandAPI)
- [https://github.com/jannyboy11/guilib/](https://github.com/jannyboy11/guilib/)
- [https://github.com/tr7zw/Item-NBT-API](https://github.com/tr7zw/Item-NBT-API)
- [https://github.com/WesJD/AnvilGUI](https://github.com/WesJD/AnvilGUI)

## いいなと思った repository

- [https://github.com/SkytAsul/GlowingEntities](https://github.com/SkytAsul/GlowingEntities)
