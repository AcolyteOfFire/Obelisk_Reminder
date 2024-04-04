package com.Obelisk_Reminder;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(Obelisk_ReminderPlugin.class);
		RuneLite.main(args);
	}
}