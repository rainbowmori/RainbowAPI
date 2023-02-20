package github.rainbowmori.rainbowapi.object.commandapi.wrappers;

import github.rainbowmori.rainbowapi.object.commandapi.arguments.PreviewInfo;
import github.rainbowmori.rainbowapi.object.commandapi.exceptions.WrapperCommandSyntaxException;

public interface PreviewableFunction<T> {

	public T generatePreview(PreviewInfo<T> info) throws WrapperCommandSyntaxException;
	
}
