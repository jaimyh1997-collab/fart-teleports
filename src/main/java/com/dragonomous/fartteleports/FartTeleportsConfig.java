package com.dragonomous.fartteleports;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(FartTeleportsConfig.GROUP)
public interface FartTeleportsConfig extends Config
{
	String GROUP = "fartteleports";

	@ConfigItem(
		keyName = "sound",
		name = "Fart",
		description = "Which fart plays when you teleport",
		position = 0
	)
	default FartSound sound()
	{
		return FartSound.RANDOM;
	}

	@Range(min = 0, max = 100)
	@ConfigItem(
		keyName = "volume",
		name = "Volume",
		description = "Fart volume (0-100)",
		position = 1
	)
	default int volume()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "otherPlayers",
		name = "Other players too",
		description = "Also replace the teleport sounds of players around you",
		position = 2
	)
	default boolean otherPlayers()
	{
		return true;
	}

	@ConfigItem(
		keyName = "extraSoundIds",
		name = "Extra sound IDs",
		description = "More sound IDs to turn into farts, separated by commas (find them with 'Show sound IDs')",
		position = 3
	)
	default String extraSoundIds()
	{
		return "";
	}

	@ConfigItem(
		keyName = "showSoundIds",
		name = "Show sound IDs",
		description = "Print the ID of every sound you hear in the chat, to find teleports that still make their normal sound",
		position = 4
	)
	default boolean showSoundIds()
	{
		return false;
	}
}
