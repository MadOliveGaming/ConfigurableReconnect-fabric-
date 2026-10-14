package madolivegaming.configurablereconnect.network;

import java.util.function.Supplier;

public class RejoinVoicechatHelper {
    private static Supplier<String> currentGroupNameSupplier;
    private static String savedGroupName;

    public static boolean get() {
        return ConfigurableReconnectModVariables.rejoin_svc;
    }

    public static void set(boolean rejoin) {
        ConfigurableReconnectModVariables.rejoin_svc = rejoin;
        ReconnectDelayConfig.save();
    }

    public static void setCurrentGroupNameSupplier(Supplier<String> supplier) {
        currentGroupNameSupplier = supplier;
    }

    
public static void saveCurrentGroup() {
    System.out.println("[Configurable Reconnect] Saving voice chat group...");
    System.out.println("[Configurable Reconnect] Rejoin enabled: "
        + ConfigurableReconnectModVariables.rejoin_svc);
    System.out.println("[Configurable Reconnect] Group supplier available: "
        + (currentGroupNameSupplier != null));

    savedGroupName = null;

    if (!ConfigurableReconnectModVariables.rejoin_svc) {
        System.out.println("[Configurable Reconnect] Rejoin disabled in settings.");
        return;
    }

    if (currentGroupNameSupplier == null) {
        System.out.println("[Configurable Reconnect] Group supplier is missing.");
        return;
    }

    savedGroupName = currentGroupNameSupplier.get();

    System.out.println("[Configurable Reconnect] Captured group: "
        + savedGroupName);
}


    public static String getSavedGroupName() {
        return savedGroupName;
    }

    public static void clearSavedGroupName() {
    	savedGroupName = null;
	}
}