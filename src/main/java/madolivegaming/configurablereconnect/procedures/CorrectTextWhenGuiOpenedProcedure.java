package madolivegaming.configurablereconnect.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import madolivegaming.configurablereconnect.network.ConfigurableReconnectModVariables;
import madolivegaming.configurablereconnect.init.ConfigurableReconnectModMenus;

public class CorrectTextWhenGuiOpenedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && _player.containerMenu instanceof ConfigurableReconnectModMenus.MenuAccessor _menu)
			_menu.sendMenuStateUpdate(_player, 2, "reconnect_delay_slider", ConfigurableReconnectModVariables.reconnect_delay, true);
	}
}