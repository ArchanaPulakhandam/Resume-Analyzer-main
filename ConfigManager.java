package com.resumeanalyzer.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static ConfigManager instance;
    private final Properties properties;

    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            System.out.println("Error loading configuration: " + ex.getMessage());
        }
    }

    // ✅ SECURE: Read Groq API key from environment variable, fallback to config file if needed
    public String getGroqApiKey() {
        // 1. Try environment variable first (safe)
        String apiKey = System.getenv("GROQ_API_KEY");

        // 2. Fallback to config.properties only if env variable not set (optional)
        if ((apiKey == null || apiKey.isEmpty()) && properties.getProperty("groq.api.key") != null) {
            apiKey = properties.getProperty("groq.api.key");
        }

        // 3. If still not set, throw an error
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException(
                "Groq API key is not set. Please define GROQ_API_KEY environment variable."
            );
        }

        return apiKey;
    }
}
