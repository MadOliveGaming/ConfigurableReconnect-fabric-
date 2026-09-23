package madolivegaming.configurablereconnect.network;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.SectionPos;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import madolivegaming.configurablereconnect.procedures.OpenModSettingsGUIProcedure;
import madolivegaming.configurablereconnect.ConfigurableReconnectMod;

public record ModSettingsScreenKeybindMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<ModSettingsScreenKeybindMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ConfigurableReconnectMod.MODID, "key_mod_settings_screen_keybind"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ModSettingsScreenKeybindMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ModSettingsScreenKeybindMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new ModSettingsScreenKeybindMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<ModSettingsScreenKeybindMessage> type() {
		return TYPE;
	}

	public static void handleData(final ModSettingsScreenKeybindMessage message, final ServerPlayNetworking.Context context) {
		context.server().execute(() -> {
			pressAction(context.player(), message.eventType, message.pressedms);
		});
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (type == 0) {
			OpenModSettingsGUIProcedure.execute();
		}
	}
}