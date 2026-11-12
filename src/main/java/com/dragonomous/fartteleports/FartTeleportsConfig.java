package com.dragonomous.fartteleports;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(FartTeleportsConfig.GROUP)
public interface FartTeleportsConfig extends Config
{
	String GROUP = "fartteleports";

	@Range(min = 0, max = 100)
	@ConfigItem(
		keyName = "volume",
		name = "Volume",
		description = "Fart volume (0-100)",
		position = 0
	)
	default int volume()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "otherPlayers",
		name = "Other players too",
		description = "Also replace the teleport sounds of players around you",
		position = 1
	)
	default boolean otherPlayers()
	{
		return true;
	}

	@ConfigItem(
		keyName = "extraSoundIds",
		name = "Extra sound IDs",
		description = "More sound IDs to turn into farts, separated by commas (see the OSRS wiki's list of sound IDs)",
		position = 2
	)
	default String extraSoundIds()
	{
		return "";
	}
}
