package madolivegaming.configurablereconnect;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ClientModInitializer;

import madolivegaming.configurablereconnect.network.ConfigurableReconnectModVariables;
import madolivegaming.configurablereconnect.init.ConfigurableReconnectModScreens;
import madolivegaming.configurablereconnect.init.ConfigurableReconnectModMenus;
import madolivegaming.configurablereconnect.init.ConfigurableReconnectModKeyMappings;

@Environment(EnvType.CLIENT)
public class ConfigurableReconnectModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Start of user code block mod constructor
		// End of user code block mod constructor
		ConfigurableReconnectModScreens.clientLoad();
		ConfigurableReconnectModMenus.clientLoad();
		ConfigurableReconnectModKeyMappings.clientLoad();
		ClientPlayNetworking.registerGlobalReceiver(ConfigurableReconnectModVariables.SavedDataSyncMessage.TYPE, ConfigurableReconnectModVariables.SavedDataSyncMessage::handleData);
		// Start of user code block mod init
		// End of user code block mod init
	}
	// Start of user code block mod methods
	// End of user code block mod methods
}