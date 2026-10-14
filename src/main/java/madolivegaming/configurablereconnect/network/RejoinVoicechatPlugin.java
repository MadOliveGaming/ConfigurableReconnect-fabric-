
package madolivegaming.configurablereconnect.network;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatClientApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientVoicechatConnectionEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;

import net.minecraft.client.Minecraft;

public class RejoinVoicechatPlugin implements VoicechatPlugin {

    @Override
    public String getPluginId() {
        return "configurable_reconnect";
    }

    private void registerClientApi(VoicechatApi api) {
        if (api instanceof VoicechatClientApi clientApi) {
            System.out.println(
                "[Configurable Reconnect] Client voice chat API registered."
            );

            RejoinVoicechatHelper.setCurrentGroupNameSupplier(() -> {
                Group group = clientApi.getGroup();
                return group == null ? null : group.getName();
            });
        } else {
            System.out.println(
                "[Configurable Reconnect] API is not a VoicechatClientApi: "
                + api.getClass().getName()
            );
        }
    }

    @Override
    public void initialize(VoicechatApi api) {
        registerClientApi(api);
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(
            ClientVoicechatConnectionEvent.class,
            this::onVoicechatConnection
        );
    }

    private void onVoicechatConnection(ClientVoicechatConnectionEvent event) {
        registerClientApi(event.getVoicechat());

        System.out.println(
            "[Configurable Reconnect] Voice chat connected: "
            + event.isConnected()
        );

        System.out.println(
            "[Configurable Reconnect] Saved group: "
            + RejoinVoicechatHelper.getSavedGroupName()
        );

        if (!event.isConnected()) {
            return;
        }

        String groupName = RejoinVoicechatHelper.getSavedGroupName();

        if (groupName == null || groupName.isBlank()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();

        client.execute(() -> {
            if (client.player == null || client.getConnection() == null) {
                System.out.println(
                    "[Configurable Reconnect] Cannot join: Minecraft is not connected."
                );
                return;
            }

            String escapedGroupName = groupName
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");

            client.getConnection().sendCommand(
                "voicechat join \"" + escapedGroupName + "\""
            );

            RejoinVoicechatHelper.clearSavedGroupName();
        });
    }
}
