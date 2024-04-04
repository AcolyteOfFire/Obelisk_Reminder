package com.Obelisk_Reminder;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("Obelisk_Reminder")
public interface Obelisk_ReminderConfig extends Config
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
		return new Color(50, 50, 50, 70);
	}

	@ConfigItem(
			keyName = "flashForCurrentObelisk",
			name = "Flash for Current Obelisk",
			description = "Whether or not the warning should flash if it is set to your current location",
			position = 2
	)
	default boolean enableRanged() {return true;}

	@ConfigItem(
			keyName = "flashAtWildernessLevel",
			name = "Flash At Wilderness Level",
			description = "Minimum level of wilderness where obelisks should flash",
			position = 3
	)
	default int flashAtWildernessLevel() {return 35;}
	@Alpha
	@ConfigItem(
			keyName = "flashColor1",
			name = "Flash color 1",
			description = "First color to flash between if 'Flash overlay' is on",
			position = 10
	)
	default Color flashColor1()
	{
		return new Color(0, 128, 255, 150);
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
		return new Color(50, 50, 50, 150);
	}


}
