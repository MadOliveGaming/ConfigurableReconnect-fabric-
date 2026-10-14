
package madolivegaming.configurablereconnect.network;

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
            return; // Keep the default value of 2 seconds for delay and false for rejoin svc
        }

        Properties properties = new Properties();

        try (InputStream input = Files.newInputStream(CONFIG_FILE)) {
            properties.load(input);

			// Here we get reconnect_delay
            String value_rd = properties.getProperty("reconnect_delay");

            if (value_rd != null) {
                double delay = Double.parseDouble(value_rd);

                ConfigurableReconnectModVariables.reconnect_delay =
                    Math.max(1, Math.min(60, delay));
            }

            // Here we get rejoin_svc
            String value_svc = properties.getProperty("rejoin_svc");

			if (value_svc != null) {
    			ConfigurableReconnectModVariables.rejoin_svc = Boolean.parseBoolean(value_svc);
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

		// Save reconnect delay to config file
        properties.setProperty(
            "reconnect_delay",
            Double.toString(
                ConfigurableReconnectModVariables.reconnect_delay
            )
        );

		// Save rejoin svc to config file
        properties.setProperty(
    		"rejoin_svc",
    		Boolean.toString(
    			ConfigurableReconnectModVariables.rejoin_svc
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
