package com.obeliskReminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Obelisk Reminder"
)
public class obeliskReminderPlugin extends Plugin
{
	public static boolean obeliskInRange = false;
	public static boolean warningActive = false;

	public static int currentObeliskID;
	public static int currentObeliskWildernessLevel;

	public static String panelText = "Current Obelisk target: None";
	@Inject
	private Client client;
	@Inject
	private obeliskReminderConfig config;
	@Inject
	private OverlayManager overlayManager;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_13 = 14829;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_19 = 14830;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_27 = 14827;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_35 = 14828;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_44 = 14826;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_50 = 14831;
	private static final int WILDERNESS_OBELISK_ACTIVE_OBJECT_ID = 14825;
	private static final int POH_OBELISK = 31554;
	private int prevVarbit = -1;

	@Inject
	obeliskReminderOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick event){
		int selectedObelisk = client.getVarbitValue(4966);
		obeliskInRange = false;
		warningActive = false;
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

						if (gameObject.getId() == WILDERNESS_OBELISK_OBJECT_ID_LVL_13 ||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_19||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_27||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_35||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_44||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_50||
								gameObject.getId()==WILDERNESS_OBELISK_ACTIVE_OBJECT_ID||
								gameObject.getId()==POH_OBELISK)
						{
							WorldPoint objectLocation = gameObject.getWorldLocation();
							int distance = playerLocation.distanceTo(objectLocation);

							if(distance <= config.obeliskDetectionRange())
							{
								obeliskInRange = true;
								String chatMessage;
								String overlayMessage;
								switch (selectedObelisk){
									case 4:
										chatMessage = "Obelisk 13";
										currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_13;
										currentObeliskWildernessLevel = 13;
										break;
									case 5:
										chatMessage = "Obelisk 19";
										currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_19;
										currentObeliskWildernessLevel = 19;
										break;
									case 2:
										chatMessage = "Obelisk 27";
										currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_27;
										currentObeliskWildernessLevel = 27;
										break;
									case 3:
										chatMessage = "Obelisk 35";
										currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_35;
										currentObeliskWildernessLevel = 35;
										break;
									case 1:
										chatMessage = "Obelisk 44";
										currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_44;
										currentObeliskWildernessLevel = 44;
										break;
									case 6:
										chatMessage = "Obelisk 50";
										currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_50;
										currentObeliskWildernessLevel = 50;
										break;
									default: {
										chatMessage = "None";
										return;
									}
								}

								overlayMessage = "Target Obelisk: " + chatMessage;
								panelText = overlayMessage;
								if(prevVarbit != selectedObelisk){
									client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Selected: " + chatMessage,"");
									prevVarbit = selectedObelisk;
								}
								if(gameObject.getId()==currentObeliskID){
									warningActive = true;
									if(currentObeliskWildernessLevel<config.flashAtWildernessLevel()){
										warningActive = false;
									}
								}
								if(gameObject.getId() == POH_OBELISK){
									if(distance<=3){
										obeliskInRange = true;
									}
									else obeliskInRange = false;
								}
							}
						}
					}
				}
			}
		}

	}

	@Provides
	obeliskReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(obeliskReminderConfig.class);
	}

}
