package madolivegaming.configurablereconnect.network;

import madolivegaming.configurablereconnect.ConfigurableReconnectMod;

public class ConfigurableReconnectModVariables {
	public static double reconnect_delay = 2.0;

	public static void variablesLoad() {
    	ReconnectDelayConfig.load();
	}
}