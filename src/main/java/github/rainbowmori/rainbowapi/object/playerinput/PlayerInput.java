package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.object.RMData;
import github.rainbowmori.rainbowapi.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerInput {

    protected final RMData data;
    protected final int getInput;

    protected final List<String> texts = new ArrayList<>();

    protected final boolean cancelable;

    public PlayerInput(@NotNull RMData rmData, int getInput, boolean cancelable) throws Exception {
        if (getInput <= 0) {
            throw  new Exception("player input は 1以上でないとエラーになります");
        }
        rmData.setPlayerInput(this);
        this.data = rmData;
        this.getInput = getInput;
        this.cancelable = cancelable;
    }

    public void input(String text) {
        if (text.equals("cancel")) {
            if(cancelable) {
                Util.send(data.getPlayer(), "<blue>チャット入力をキャンセルしました");
                cleared();
            }else Util.send(data.getPlayer(), "<red>この入力はキャンセルできません");
            return;
        }
        get(text);
    }

    protected abstract void get(String text);

    public void cleared() {
        data.clearPlayerInput();
    }
}