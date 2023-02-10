package github.rainbowmori.rainbowapi.util;

import github.rainbowmori.rainbowapi.RMHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * よく使うメゾットがある
 */

public class Util {

    public static final McUtil util = RMHome.getRainbowAPI().mcUtil;

    public static Component mm(Object str) {
        return IsObjectUtil.IsComponent(str) ? ((Component) str) : MiniMessage.miniMessage().deserialize(String.valueOf(str));
    }

    /**
     * listの内容をComponentに変換
     * @param list 変換したいlist
     * @return List<Component>
     */

    public static List<Component> ListMM(List<?> list) {
        return list.stream().map(Util::mm).collect(Collectors.toList());
    }

    /**
     * {@link Component}をStringに変換
     * @param str 変換したいコンポーネント
     * @return 変換されたString
     */

    public static String serialize(Component str) {
        return MiniMessage.miniMessage().serialize(str);
    }
}
