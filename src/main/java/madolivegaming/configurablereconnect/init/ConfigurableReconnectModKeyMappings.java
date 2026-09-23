/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package madolivegaming.configurablereconnect.init;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import madolivegaming.configurablereconnect.network.ModSettingsScreenKeybindMessage;

@Environment(EnvType.CLIENT)
public class ConfigurableReconnectModKeyMappings {
	public static final KeyMapping MOD_SETTINGS_SCREEN_KEYBIND = new KeyMapping("key.configurable_reconnect.mod_settings_screen_keybind", GLFW.GLFW_KEY_NUM_LOCK, KeyMapping.Category.MISC) {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPlayNetworking.send(new ModSettingsScreenKeybindMessage(0, 0));
				ModSettingsScreenKeybindMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	public static void clientLoad() {
		KeyMappingHelper.registerKeyMapping(MOD_SETTINGS_SCREEN_KEYBIND);
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if (client.gui.screen() == null) {
				MOD_SETTINGS_SCREEN_KEYBIND.consumeClick();
			}
		});
	}
}