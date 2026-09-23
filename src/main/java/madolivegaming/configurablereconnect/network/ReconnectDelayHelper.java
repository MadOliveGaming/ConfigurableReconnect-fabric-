package madolivegaming.configurablereconnect.network;

public class ReconnectDelayHelper {

    public static double get() {
        return ConfigurableReconnectModVariables.MapVariables.clientSide.reconnect_delay;
    }

    public static void set(double delay) {
        ConfigurableReconnectModVariables.MapVariables.clientSide.reconnect_delay = delay;
    }
}