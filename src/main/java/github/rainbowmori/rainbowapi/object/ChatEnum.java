package github.rainbowmori.rainbowapi.object;

public enum ChatEnum {
    NOT_MATERIAL("<red>そのマテリアルはありません"),
    NOT_FUNCTION("<red>その機能はありません"),
    NOT_INTEGER("<red>数字ではありません"),
    NOT_ENOUGH_WRITE("<red>記入が足りません"),
    NOT_HAS_ITEM("<red>アイテムを持ってください"),
    NOT_PERMISSION("<red>あなたは必要な権限を持っていません"),
    NOT_PLAYER("<red>プレイヤーではありません");

    public final String text;

    ChatEnum(String s) {
        this.text = s;
    }
}
