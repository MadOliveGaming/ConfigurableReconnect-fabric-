
package madolivegaming.configurablereconnect.network;

import madolivegaming.configurablereconnect.ConfigurableReconnectMod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

public class ReconnectDelayConfig {

    private static final Path CONFIG_FILE =
        FabricLoader.getInstance()
            .getConfigDir()
            .resolve("configurable_reconnect.properties");

    public static void load() {
        if (!Files.exists(CONFIG_FILE)) {
            return; // Keep the default value of 2 seconds.
        }

        Properties properties = new Properties();

        try (InputStream input = Files.newInputStream(CONFIG_FILE)) {
            properties.load(input);

            String value = properties.getProperty("reconnect_delay");

            if (value != null) {
                double delay = Double.parseDouble(value);

                ConfigurableReconnectModVariables.reconnect_delay =
                    Math.max(1, Math.min(60, delay));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println(
                "[Configurable Reconnect] Could not load config: "
                    + e.getMessage()
            );
        }
    }

    public static void save() {
        Properties properties = new Properties();

        properties.setProperty(
            "reconnect_delay",
            Double.toString(
                ConfigurableReconnectModVariables.reconnect_delay
            )
        );

        try {
            Files.createDirectories(CONFIG_FILE.getParent());

            try (OutputStream output = Files.newOutputStream(CONFIG_FILE)) {
                properties.store(output, "Configurable Reconnect settings");
            }
        } catch (IOException e) {
            System.err.println(
                "[Configurable Reconnect] Could not save config: "
                    + e.getMessage()
            );
        }
    }
}
