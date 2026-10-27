package com.uniezz.qa.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Resolves configuration in this order (first match wins):
 * 1. JVM system property      -Dapi.base.url=...
 * 2. Environment variable     API_BASE_URL=...   (used for CI secrets)
 * 3. config/{env}.properties  where env comes from -Denv (default: dev)
 */
public final class Config {

    private static final String ENV = System.getProperty("env", "dev");
    private static final Properties PROPS = load(ENV);

    private Config() {
    }

    public static String env() {
        return ENV;
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key.toUpperCase().replace('.', '_'));
        }
        if (value == null) {
            value = PROPS.getProperty(key);
        }
        if (value == null) {
            throw new IllegalStateException("Missing config key '" + key + "' for env '" + ENV + "'");
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        try {
            return get(key);
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    private static Properties load(String env) {
        String path = "config/" + env + ".properties";
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Config file not found on classpath: " + path);
            }
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + path, e);
        }
    }
}
