# RainbowAPI

自分の作るプラグイン用に作ってあるAPI Plugin

自分が使う機能が作ってある


### jitpack gradle SNAPSHOT
```
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.rainbowmori:RainbowAPI:master-SNAPSHOT'
}    
```

### 使い方
```
public final class TEST extends JavaPlugin {

    private static TEST plugin;

    private static RainbowAPI rainbowAPI;

    @Override
    public void onEnable() {
        plugin = this;
        rainbowAPI = new RainbowAPI(plugin, "prefix");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static TEST getPlugin() {
        return plugin;
    }

    public static RainbowAPI getRainbowAPI() {
        return rainbowAPI;
    }

}
```

