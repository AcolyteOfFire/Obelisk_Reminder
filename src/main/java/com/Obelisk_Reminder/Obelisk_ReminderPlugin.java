package com.Obelisk_Reminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.print.attribute.standard.Destination;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import java.awt.event.KeyEvent;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.achievementdiary.diaries.WildernessDiaryRequirement;
import net.runelite.client.ui.overlay.OverlayManager;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
@PluginDescriptor(
	name = "Obelisk Reminder"
)
public class Obelisk_ReminderPlugin extends Plugin implements java.awt.event.KeyListener
{
	public static boolean obeliskInRange = false;
	public static boolean warningActive = false;

	public static int currentObeliskID;
	public static int currentObeliskWildernessLevel;

	public static String panelText = "Current target: unknown";
	public static boolean isWidgetOpen = false;

	@Inject
	private Client client;
	@Inject
	private Obelisk_ReminderConfig config;
	@Inject
	private OverlayManager overlayManager;

	private static final int WILDERNESS_OBELISK_WIDGET_ID = 12255235;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_13 = 14829;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_19 = 14830;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_27 = 14827;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_35 = 14828;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_44 = 14826;
	private static final int WILDERNESS_OBELISK_OBJECT_ID_LVL_50 = 14831;
	private final Map<Integer,String> previousWidgetStates = new HashMap<>();

	@Inject Obelisk_ReminderOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		client.getCanvas().addKeyListener(this);
	}

	@Override
	protected void shutDown() throws Exception
	{
		client.getCanvas().removeKeyListener(this);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			overlayManager.add(overlay);
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

						if (gameObject.getId() == WILDERNESS_OBELISK_OBJECT_ID_LVL_13 ||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_19||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_27||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_35||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_44||
								gameObject.getId()==WILDERNESS_OBELISK_OBJECT_ID_LVL_50)
						{
							WorldPoint objectLocation = gameObject.getWorldLocation();
							int distance = playerLocation.distanceTo(objectLocation);

							if(distance <= config.obeliskDetectionRange())
							{
								obeliskInRange = true;
								if(gameObject.getId()==currentObeliskID){
									warningActive = true;
									if(currentObeliskWildernessLevel<config.flashAtWildernessLevel()){
										warningActive = false;
									}
								}
								else warningActive = false;
							}
							else obeliskInRange = false;

						}
					}
				}
			}
		}
		if (pendingChatMessage != null) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", pendingChatMessage, "");
			pendingChatMessage = null;  // Clear after sending
		}
		if (pendingPanelText != null) {
			panelText=pendingPanelText;
			pendingChatMessage = null;  // Clear after sending
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (isWidgetOpen) {
			int keyCode = e.getKeyCode();

			switch (keyCode) {
				case KeyEvent.VK_1: {
					onSelection(1);
					break;
				}
				case KeyEvent.VK_2: {
					onSelection(2);
					break;
				}
				case KeyEvent.VK_3: {
					onSelection(3);
					break;
				}
				case KeyEvent.VK_4: {
					onSelection(4);
					break;
				}
				case KeyEvent.VK_5: {
					onSelection(5);
					break;
				}
				case KeyEvent.VK_6: {
					onSelection(6);
					break;
				}
				default:
					break;
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e){

	}
	@Override
	public void keyReleased(KeyEvent e){

	}
	@Subscribe
	public void onClientTick(ClientTick event){
		Widget widget = client.getWidget(WILDERNESS_OBELISK_WIDGET_ID);
		if(widget != null){
			isWidgetOpen = true;
			//debugText = "Widget: yes";
		}
		else{
			isWidgetOpen = false;
			//debugText = "Widget: no";
		}

	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event){
			String regex = "<col=735a28>(\\d+)</col>";
			Pattern pattern = Pattern.compile(regex);
			if(event.getWidget().getId() == WILDERNESS_OBELISK_WIDGET_ID){
				int optionClicked;
				String widgetText = event.getWidget().getText();
				Matcher matcher = pattern.matcher(widgetText);
				if(matcher.find()){
					String numberStr = matcher.group(1);
					optionClicked = Integer.parseInt(numberStr);
				}
				else return;

				onSelection(optionClicked);
			}
	}
	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event){

	}
	private String pendingChatMessage;
	private String pendingPanelText;
	private void onSelection(int sel){
		String chatMessage;
		String overlayMessage;
		switch (sel){
			case 1:
				chatMessage = "Obelisk 13";
				currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_13;
				currentObeliskWildernessLevel = 13;
				break;
			case 2:
				chatMessage = "Obelisk 19";
				currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_19;
				currentObeliskWildernessLevel = 19;
				break;
			case 3:
				chatMessage = "Obelisk 27";
				currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_27;
				currentObeliskWildernessLevel = 27;
				break;
			case 4:
				chatMessage = "Obelisk 35";
				currentObeliskID = WILDERNESS_OBELISK_OBJECT_ID_LVL_35;
				currentObeliskWildernessLevel = 35;
				break;
			case 5:
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
				return;
			}
		}
		pendingPanelText = "Current target: "+chatMessage;
		pendingChatMessage = chatMessage;
	}

	@Provides
	Obelisk_ReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Obelisk_ReminderConfig.class);
	}

}
