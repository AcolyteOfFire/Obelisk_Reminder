package com.obeliskreminder;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class obeliskReminderTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(obeliskReminderPlugin.class);
		RuneLite.main(args);
	}
}