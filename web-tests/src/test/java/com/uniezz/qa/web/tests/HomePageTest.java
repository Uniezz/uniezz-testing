package com.uniezz.qa.web.tests;

import com.uniezz.qa.core.TestTags;
import com.uniezz.qa.web.BaseWebTest;
import com.uniezz.qa.web.pages.HomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag(TestTags.SMOKE)
class HomePageTest extends BaseWebTest {

    @Test
    void homePageLoads() {
        HomePage home = new HomePage(page).open();
        assertThat(home.title()).isNotBlank();
    }
}
