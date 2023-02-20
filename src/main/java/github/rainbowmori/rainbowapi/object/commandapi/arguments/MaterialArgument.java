package github.rainbowmori.rainbowapi.object.commandapi.arguments;

import org.bukkit.Material;

import java.util.Arrays;

public class MaterialArgument extends CustomArgument<Material,String>{

	public MaterialArgument(String nodeName) {
		super(new StringArgument(nodeName), info -> {
			try {
				return Material.valueOf(info.input());
			} catch (IllegalArgumentException e) {
				throw new CustomArgumentException(new MessageBuilder("Unknown Material: ").appendArgInput());
			}
		});
		replaceSuggestions(ArgumentSuggestions.strings(info ->
			Arrays.stream(Material.values()).map(Material::name).toArray(String[]::new)));
	}

}
