package madolivegaming.configurablereconnect.network;

public class ReconnectDelayHelper {

    public static double get() {
        return ConfigurableReconnectModVariables.reconnect_delay;
    }

    public static void set(double delay) {
        ConfigurableReconnectModVariables.reconnect_delay =
            Math.max(1, Math.min(60, delay));
    }
}