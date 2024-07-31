package com.Obelisk_Reminder;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Obelisk_ReminderTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(Obelisk_ReminderPlugin.class);
		RuneLite.main(args);
	}
}