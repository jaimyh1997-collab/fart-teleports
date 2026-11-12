package com.dragonomous.fartteleports;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.audio.AudioPlayer;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Fart Teleports",
	description = "Replaces teleport sound effects with a fart",
	tags = {"fart", "teleport", "sound", "funny", "meme", "dragonomous"}
)
public class FartTeleportsPlugin extends Plugin
{
	/**
	 * Teleport sounds from the OSRS wiki's list of sound IDs:
	 * 198 quick_teleport, 200 teleport_all (spellbook, jewellery, tablets), 201 teleport_reverse,
	 * 204 wilderness_teleport, 965 poh_tablet_break_teleport, 984 poh_teleport, 1098 FT_fairy_teleport,
	 * 5324 lotg_fairy_teleport, 5350 portal_teleport, 6655 dark_teleport_2, 6657 dark_teleport_1.
	 */
	private static final Set<Integer> TELEPORT_SOUNDS = ImmutableSet.of(
		198, 200, 201, 204, 965, 984, 1098, 5324, 5350, 6655, 6657);

	private static final String FART_SOUND = "fart.wav";

	/** One teleport can fire several sounds; fart at most once per this many ms. */
	private static final long FART_COOLDOWN_MS = 1200;

	@Inject
	private Client client;

	@Inject
	private FartTeleportsConfig config;

	@Inject
	private AudioPlayer audioPlayer;

	@Inject
	private ScheduledExecutorService executor;

	private final Set<Integer> extraSounds = new HashSet<>();
	private long lastFart;

	@Provides
	FartTeleportsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FartTeleportsConfig.class);
	}

	@Override
	protected void startUp()
	{
		parseExtraSounds();
	}

	@Override
	protected void shutDown()
	{
		extraSounds.clear();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (FartTeleportsConfig.GROUP.equals(event.getGroup()))
		{
			parseExtraSounds();
		}
	}

	@Subscribe
	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int id = event.getSoundId();
		showId("sound", id);
		if (isTeleportSound(id))
		{
			event.consume();
			fart();
		}
	}

	@Subscribe
	public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed event)
	{
		int id = event.getSoundId();
		showId("area sound", id);
		if (!isTeleportSound(id))
		{
			return;
		}

		Actor source = event.getSource();
		boolean mine = source == null || source == client.getLocalPlayer();
		if (!mine && !(source instanceof Player && config.otherPlayers()))
		{
			return;
		}
		event.consume();
		fart();
	}

	private boolean isTeleportSound(int id)
	{
		return TELEPORT_SOUNDS.contains(id) || extraSounds.contains(id);
	}

	private void fart()
	{
		long now = System.currentTimeMillis();
		if (now - lastFart < FART_COOLDOWN_MS || config.volume() <= 0)
		{
			return;
		}
		lastFart = now;

		// 100% = full volume, lower percentages are converted to decibels.
		float gain = (float) (20 * Math.log10(config.volume() / 100.0));

		executor.submit(() ->
		{
			try
			{
				audioPlayer.play(FartTeleportsPlugin.class, FART_SOUND, gain);
			}
			catch (Exception e)
			{
				log.warn("Could not play fart sound", e);
			}
		});
	}

	private void showId(String kind, int id)
	{
		if (config.showSoundIds())
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Fart Teleports: " + kind + " " + id, null);
		}
	}

	private void parseExtraSounds()
	{
		extraSounds.clear();
		for (String part : config.extraSoundIds().split(","))
		{
			try
			{
				String trimmed = part.trim();
				if (!trimmed.isEmpty())
				{
					extraSounds.add(Integer.parseInt(trimmed));
				}
			}
			catch (NumberFormatException e)
			{
				// Ignore anything that isn't a number.
			}
		}
	}
}
