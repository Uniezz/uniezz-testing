package com.uniezz.qa.mobile;

import com.uniezz.qa.mobile.driver.DriverFactory;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Fresh app session per test so tests stay independent.
 */
public abstract class BaseMobileTest {

    protected AppiumDriver driver;

    @BeforeEach
    void startSession() {
        driver = DriverFactory.create();
    }

    @AfterEach
    void endSession() {
        if (driver != null) {
            driver.quit();
        }
    }
}
