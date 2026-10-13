
package madolivegaming.configurablereconnect.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import madolivegaming.configurablereconnect.network.ConfigurableReconnectModVariables;
import madolivegaming.configurablereconnect.network.ReconnectDelayConfig;
import madolivegaming.configurablereconnect.init.ConfigurableReconnectModMenus;

public class SetRecconectDelayOnGUIClosesProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;

        double newDelay =
            (entity instanceof Player _entity0
                && _entity0.containerMenu instanceof ConfigurableReconnectModMenus.MenuAccessor _menu0)
                ? _menu0.getMenuState(2, "reconnect_delay_slider", 0.0)
                : ConfigurableReconnectModVariables.reconnect_delay;

        // Clamp the actual slider value.
        newDelay = Math.max(1, Math.min(60, newDelay));

        // Update the session variable.
        ConfigurableReconnectModVariables.reconnect_delay = newDelay;

        // Persist it across Minecraft restarts.
        ReconnectDelayConfig.save();
    }
}
