package com.Obelisk_Reminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.achievementdiary.diaries.WildernessDiaryRequirement;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
	name = "Obelisk Reminder"
)
public class Obelisk_ReminderPlugin extends Plugin
{
	public boolean obeliskInRange = false;
	public int currentObeliskMemory = -1;

	@Inject
	private Client client;

	@Inject
	private Obelisk_ReminderConfig config;

	@Inject
	private static final int WILDERNESS_OBELISK_WIDGET_ID = 12255235;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_13 = 14829;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_19 = 14830;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_27 = 14827;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_35 = 14828;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_44 = 14826;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_50 = 14831;

	@Inject Obelisk_ReminderOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{

	}

	@Override
	protected void shutDown() throws Exception
	{

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			
		}
	}
	@Subscribe
	public void onGameTick(GameTick event){
		WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
		for (Tile[][] tileArray : client.getScene().getTiles())
		{
			for (Tile[] tileRow : tileArray)
			{
				for (Tile tile : tileRow)
				{
					if (tile == null)
					{
						continue;
					}

					for (GameObject gameObject : tile.getGameObjects())
					{
						if (gameObject == null)
						{
							continue;
						}

						if (gameObject.getId() == 14829)
						{
							WorldPoint objectLocation = gameObject.getWorldLocation();
							int distance = playerLocation.distanceTo(objectLocation);

							if (distance <= 8)
							{
								obeliskInRange = true;
							}
							else obeliskInRange = false;
						}
					}
				}
			}
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event){
			if(event.getWidget().getId() == WILDERNESS_OBELISK_WIDGET_ID){
				String option = event.getMenuOption();
				String widgetText = event.getWidget().getText();
				client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Selected: "+ widgetText,"");
			}
	}

	@Provides
	Obelisk_ReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Obelisk_ReminderConfig.class);
	}

}
