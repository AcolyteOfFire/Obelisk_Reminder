package com.obeliskreminder;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;


class obeliskReminderOverlay extends OverlayPanel
{
    private final Client client;
    private final obeliskReminderConfig config;

    @Inject
    private obeliskReminderOverlay(Client client, obeliskReminderConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.BOTTOM_RIGHT);
        isMovable();
        isResizable();
        isResettable();
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();
        if (obeliskReminderPlugin.obeliskInRange) {
            panelComponent.getChildren().add(LineComponent.builder().left(obeliskReminderPlugin.panelText).build());
            if (config.shouldFlash()) {
                if (obeliskReminderPlugin.warningActive){
                    if (client.getGameCycle() % 40 >= 20) {
                        panelComponent.setBackgroundColor(config.flashColor1());
                    }
                    else {
                    panelComponent.setBackgroundColor(config.flashColor2());
                    }
                }
                else panelComponent.setBackgroundColor(config.textBoxColor());
            }
            else {
                panelComponent.setBackgroundColor(config.textBoxColor());
            }
            setPosition(OverlayPosition.BOTTOM_RIGHT);
            return panelComponent.render(graphics);
        }
    return panelComponent.render(graphics);
    }
}
