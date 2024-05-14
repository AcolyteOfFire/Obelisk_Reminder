package com.Obelisk_Reminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;


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

						if (gameObject.getId() == SPECIFIC_OBJECT_ID)
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
		if(event.getMenuAction() == MenuAction.WIDGET_FIRST_OPTION){
			switch (event.getMenuOption()){
			case "1":
				currentObeliskMemory =1;
				break;
			case "2":
				currentObeliskMemory =2;
				break;
			case "3":
				currentObeliskMemory =3;
				break;
			case "4":
				currentObeliskMemory =4;
				break;
			case "5":
				currentObeliskMemory =5;
				break;
			case "6":
				currentObeliskMemory =6;
				break;
			default:
				break;
			}
		}
	}

	@Provides
	Obelisk_ReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Obelisk_ReminderConfig.class);
	}
}
