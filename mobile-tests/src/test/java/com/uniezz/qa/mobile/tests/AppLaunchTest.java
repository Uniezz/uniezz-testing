package com.uniezz.qa.mobile.tests;

import com.uniezz.qa.core.TestTags;
import com.uniezz.qa.mobile.BaseMobileTest;
import com.uniezz.qa.mobile.screens.WelcomeScreen;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag(TestTags.SMOKE)
class AppLaunchTest extends BaseMobileTest {

    @Test
    void appOpensOnWelcomeScreen() {
        assertThat(new WelcomeScreen(driver).isDisplayed()).isTrue();
    }
}
