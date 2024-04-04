package com.Obelisk_Reminder;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;


public class Obelisk_ReminderOverlay {
    @Inject
    private Obelisk_ReminderOverlay(Client client, Obelisk_ReminderConfig config)
    {
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildern().clear();

    }
}
