package madolivegaming.configurablereconnect;

import madolivegaming.configurablereconnect.network.ConfigurableReconnectModVariables;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReconnectButtonHandler {

    private static final ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor();

    public static void init() {

        ScreenEvents.AFTER_INIT.register(
            (client, screen, scaledWidth, scaledHeight) -> {

                if (!(screen instanceof PauseScreen)) {
                    return;
                }

                if (client.hasSingleplayerServer()) {
                    return;
                }

                ServerData serverData =
                    client.getCurrentServer();

                if (serverData == null) {
                    return;
                }

                Button disconnectButton =
                    findDisconnectButton(screen);

                if (disconnectButton == null) {
                    return;
                }

                ServerData serverToReconnect =
                    serverData;

                Button reconnectButton = Button.builder(
                    Component.literal("Reconnect"),
                    button -> {

                        double reconnectDelay =
                            ConfigurableReconnectModVariables
                                .MapVariables
                                .get(null)
                                .reconnect_delay;

                        reconnectDelay =
                            Math.max(0.0, reconnectDelay);

                        /*
                         * Trigger the actual vanilla Disconnect button.
                         */
                        disconnectButton.onPress(
                            new MouseButtonInfo(0, 0)
                        );

                        /*
                         * Do not try to detect which screen Minecraft
                         * switched to or whether getCurrentServer()
                         * has been cleared.
                         *
                         * Just wait the configured amount of time,
                         * exactly like the original working reconnect
                         * implementation did.
                         */
                        scheduler.schedule(
                            () -> {

                                client.execute(() -> {

                                    Screen currentScreen =
                                        client.gui.screen();

                                    ServerAddress address =
                                        createServerAddress(
                                            serverToReconnect.ip
                                        );

                                    ConnectScreen.startConnecting(
                                        currentScreen,
                                        client,
                                        address,
                                        serverToReconnect,
                                        false,
                                        null
                                    );
                                });

                            },
                            (long) (reconnectDelay * 1000.0),
                            TimeUnit.MILLISECONDS
                        );
                    }
                ).build();

                positionButtons(
                    screen,
                    disconnectButton,
                    reconnectButton
                );
            }
        );
    }

    private static Button findDisconnectButton(
        Screen screen
    ) {

        try {

            java.lang.reflect.Field childrenField =
                Screen.class.getDeclaredField("children");

            childrenField.setAccessible(true);

            @SuppressWarnings("unchecked")
            java.util.List<
                net.minecraft.client.gui.components.events.GuiEventListener
            > children =
                (java.util.List<
                    net.minecraft.client.gui.components.events.GuiEventListener
                >) childrenField.get(screen);

            for (var child : children) {

                if (child instanceof Button button) {

                    if (button.getMessage()
                        .getString()
                        .equals("Disconnect")) {

                        return button;
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    private static ServerAddress createServerAddress(
        String address
    ) {

        String host = address;
        int port = 25565;

        int colon =
            address.lastIndexOf(':');

        if (colon > 0 &&
            colon < address.length() - 1) {

            String possiblePort =
                address.substring(colon + 1);

            try {

                port =
                    Integer.parseInt(
                        possiblePort
                    );

                host =
                    address.substring(
                        0,
                        colon
                    );

            } catch (NumberFormatException ignored) {
            }
        }

        return new ServerAddress(
            host,
            port
        );
    }

    private static void positionButtons(
        Screen screen,
        Button disconnectButton,
        Button reconnectButton
    ) {

        int originalX =
            disconnectButton.getX();

        int originalY =
            disconnectButton.getY();

        int originalWidth =
            disconnectButton.getWidth();

        int originalHeight =
            disconnectButton.getHeight();

        int gap = 10;

        int halfWidth =
            (originalWidth - gap) / 2;

        reconnectButton.setX(
            originalX
        );

        reconnectButton.setY(
            originalY
        );

        reconnectButton.setWidth(
            halfWidth
        );

        reconnectButton.setHeight(
            originalHeight
        );

        disconnectButton.setX(
            originalX + halfWidth + gap
        );

        disconnectButton.setY(
            originalY
        );

        disconnectButton.setWidth(
            halfWidth
        );

        disconnectButton.setHeight(
            originalHeight
        );

        addButton(
            screen,
            reconnectButton
        );
    }

    private static void addButton(
        Screen screen,
        Button button
    ) {

        try {

            Method method =
                Screen.class.getDeclaredMethod(
                    "addRenderableWidget",
                    net.minecraft.client.gui.components.events.GuiEventListener.class
                );

            method.setAccessible(true);

            method.invoke(
                screen,
                button
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}