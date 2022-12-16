package github.rainbowmori.rainbowapi.manager;

public enum ChatEnum {
    NOT_PLAYER("<red>プレイヤーではありません");

    public final String text;

    ChatEnum(String s) {
        this.text = s;
    }
}
