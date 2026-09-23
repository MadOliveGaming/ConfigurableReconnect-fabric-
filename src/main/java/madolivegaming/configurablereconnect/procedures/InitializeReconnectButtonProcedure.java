package madolivegaming.configurablereconnect.procedures;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class InitializeReconnectButtonProcedure {
	public static boolean eventResult = true;

	public InitializeReconnectButtonProcedure() {
		ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
			execute();
		});
	}

	public static void execute() {
		madolivegaming.configurablereconnect.ReconnectButtonHandler.init();
	}
}