
package github.rainbowmori.rainbowapi.object.command;

import com.mojang.brigadier.Message;

public interface IStringTooltip {

	String getSuggestion();

	Message getTooltip();

}
