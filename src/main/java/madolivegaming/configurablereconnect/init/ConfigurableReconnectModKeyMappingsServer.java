/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package madolivegaming.configurablereconnect.init;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import madolivegaming.configurablereconnect.network.ModSettingsScreenKeybindMessage;

public class ConfigurableReconnectModKeyMappingsServer {
	public static void serverLoad() {
		PayloadTypeRegistry.serverboundPlay().register(ModSettingsScreenKeybindMessage.TYPE, ModSettingsScreenKeybindMessage.STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(ModSettingsScreenKeybindMessage.TYPE, ModSettingsScreenKeybindMessage::handleData);
	}
}