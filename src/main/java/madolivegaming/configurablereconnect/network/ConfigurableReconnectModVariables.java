package madolivegaming.configurablereconnect.network;

import madolivegaming.configurablereconnect.network.ReconnectDelayConfig;

public class ConfigurableReconnectModVariables {
	public static double reconnect_delay = 2.0;
	public static boolean rejoin_svc = false;

	public static void variablesLoad() {
		ReconnectDelayConfig.load();
	}
}