package github.rainbowmori.rainbowapi.object;

import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.object.customblock.CustomBlockData;

public class DataManager {

  public final CustomBlockData CustomBlockData;

  public DataManager(RainbowAPI rainbowAPI) {
    CustomBlockData = new CustomBlockData(rainbowAPI);
  }

  public void save() {
    CustomBlockData.saveFile();
  }

}
