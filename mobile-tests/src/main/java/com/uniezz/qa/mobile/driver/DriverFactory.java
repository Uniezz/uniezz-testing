package com.uniezz.qa.mobile.driver;

import com.uniezz.qa.core.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Creates an Android or iOS driver depending on -Dmobile.platform (android | ios).
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static AppiumDriver create() {
        URL server = appiumUrl();
        return switch (Config.get("mobile.platform", "android")) {
            case "ios" -> new IOSDriver(server, new XCUITestOptions()
                    .setDeviceName(Config.get("mobile.ios.device"))
                    .setApp(absolute(Config.get("mobile.ios.app")))
                    .setNewCommandTimeout(Duration.ofSeconds(120)));
            case "android" -> new AndroidDriver(server, new UiAutomator2Options()
                    .setUdid(Config.get("mobile.android.device"))
                    .setApp(absolute(Config.get("mobile.android.app")))
                    .setNewCommandTimeout(Duration.ofSeconds(120)));
            default -> throw new IllegalArgumentException("Unknown mobile.platform: " + Config.get("mobile.platform"));
        };
    }

    private static URL appiumUrl() {
        try {
            return URI.create(Config.get("mobile.appium.url")).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid mobile.appium.url", e);
        }
    }

    private static String absolute(String path) {
        return Paths.get(path).toAbsolutePath().toString();
    }
}
