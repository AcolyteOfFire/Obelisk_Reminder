package com.obeliskreminder;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("Obelisk_Reminder")
public interface obeliskReminderConfig extends Config
{
	@Alpha
	@ConfigItem(
			keyName = "textBoxColor",
			name = "Text Box Color",
			description = "the normal color of the text box",
			position =1
	)
	default Color textBoxColor()
	{
		return new Color(0, 0, 0, 70);
	}

	@ConfigItem(
			keyName = "shouldFlash",
			name = "Warning Flash",
			description = "Whether or not the warning should flash if you are in range of the destination obelisk",
			position = 2
	)
	default boolean shouldFlash() {return true;}


	@ConfigItem(
			keyName = "flashAtWildernessLevel",
			name = "Flash At Wilderness Level",
			description = "Minimum level of wilderness where obelisks should flash",
			position = 3
	)
	default int flashAtWildernessLevel() {return 27;}
	@ConfigItem(
			keyName = "obeliskDetectionRange",
			description = "Displays obelisk information when obelisks are in this range",
			name = "Obelisk Detection Range"
	)
	default int obeliskDetectionRange() {return 10;}
	@Alpha
	@ConfigItem(
			keyName = "flashColor1",
			name = "Flash color 1",
			description = "First color to flash between if 'Flash overlay' is on",
			position = 10
	)
	default Color flashColor1()
	{
		return new Color(210, 0, 0, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "flashColor2",
			name = "Flash color 2",
			description = "Second color to flash between if 'Flash overlay' is on",
			position = 11
	)
	default Color flashColor2()
	{
		return new Color(150, 0, 0, 150);
	}


}
