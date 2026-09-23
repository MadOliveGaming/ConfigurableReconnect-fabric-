package madolivegaming.configurablereconnect.procedures;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import madolivegaming.configurablereconnect.client.gui.ConfigurableReconnectGUIScreen;
import madolivegaming.configurablereconnect.world.inventory.ConfigurableReconnectGUIMenu;

public class OpenReconnectConfigProcedure {

    public static void execute() {

        Minecraft minecraft =
            Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        ConfigurableReconnectGUIMenu menu =
            new ConfigurableReconnectGUIMenu(
                0,
                minecraft.player.getInventory()
            );

        minecraft.gui.setScreen(
    		new ConfigurableReconnectGUIScreen(
        	menu,
        	minecraft.player.getInventory(),
        	Component.literal("Configurable Reconnect")
    		)
		);
    }
}