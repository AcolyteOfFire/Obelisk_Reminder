package com.Obelisk_Reminder;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;


class Obelisk_ReminderOverlay extends OverlayPanel
{
    private final Client client;
    private final Obelisk_ReminderConfig config;

    @Inject
    private Obelisk_ReminderOverlay(Client client, Obelisk_ReminderConfig config)
    {
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();

        if (config.shouldFlash())
        {
            if (client.getGameCycle() % 40 >= 20)
            {
                panelComponent.setBackgroundColor(config.flashColor1());
            }
            else
            {
                panelComponent.setBackgroundColor(config.flashColor2());
            }
        }
        else
        {
            panelComponent.setBackgroundColor(config.flashColor1());
        }
        setPosition(OverlayPosition.BOTTOM_RIGHT);
        return panelComponent.render(graphics);

    }
}
