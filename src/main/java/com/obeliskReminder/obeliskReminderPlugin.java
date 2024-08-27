package com.obeliskReminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private List<GameObject> obelisks = new ArrayList<GameObject>();

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
		obelisks.clear();
	}

	@Subscribe
	public void onGameTick(GameTick event){
		if(obelisks.isEmpty()) return;
		int selectedObelisk = client.getVarbitValue(4966);
		obeliskInRange = false;
		warningActive = false;
		WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
		for(GameObject obelisk : obelisks){
			if (isObelisk(obelisk.getId()))
			{
				WorldPoint objectLocation = obelisk.getWorldLocation();
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
					if(obelisk.getId()==currentObeliskID){
						warningActive = true;
						if(currentObeliskWildernessLevel<config.flashAtWildernessLevel()){
							warningActive = false;
						}
					}
					if(obelisk.getId() == POH_OBELISK){
						if(distance<=3){
							obeliskInRange = true;
						}
						else obeliskInRange = false;
					}
				}

			}

	}

	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event){
		GameObject gameObject = event.getGameObject();
		if (isObelisk(gameObject.getId())) {
			obelisks.add(gameObject);
		}
	}
	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event){
		GameObject gameObject = event.getGameObject();
		if (isObelisk(gameObject.getId())) {
			obelisks.remove(gameObject);
		}
	}
	@Subscribe
	public void onGameStateChanged(GameStateChanged event){
		if(event.getGameState().equals(GameState.LOADING)){
			obelisks.clear();
		}
	}

	@Provides
	obeliskReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(obeliskReminderConfig.class);
	}

	private boolean isObelisk(int objectId) {
		return (objectId >= WILDERNESS_OBELISK_ACTIVE_OBJECT_ID && objectId <= WILDERNESS_OBELISK_OBJECT_ID_LVL_50) || objectId == POH_OBELISK;
	}

}
