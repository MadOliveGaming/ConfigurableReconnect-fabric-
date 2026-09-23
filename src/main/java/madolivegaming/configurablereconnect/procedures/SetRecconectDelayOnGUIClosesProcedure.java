package madolivegaming.configurablereconnect.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import madolivegaming.configurablereconnect.init.ConfigurableReconnectModMenus;

public class SetRecconectDelayOnGUIClosesProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double newDelay = 0;
		if (newDelay < 1) {
			newDelay = 1;
		} else if (newDelay > 60) {
			newDelay = 60;
		}
		newDelay = (entity instanceof Player _entity0 && _entity0.containerMenu instanceof ConfigurableReconnectModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(2, "reconnect_delay_slider", 0.0) : 0.0;
	}
}