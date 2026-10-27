package com.uniezz.qa.mobile.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Screen Object: ask the app devs to add the same accessibility IDs on Android and iOS
 * so one locator works on both platforms.
 */
public class WelcomeScreen {

    private static final By ROOT = AppiumBy.accessibilityId("welcome-screen");

    private final AppiumDriver driver;

    public WelcomeScreen(AppiumDriver driver) {
        this.driver = driver;
    }

    public boolean isDisplayed() {
        return driver.findElement(ROOT).isDisplayed();
    }
}
