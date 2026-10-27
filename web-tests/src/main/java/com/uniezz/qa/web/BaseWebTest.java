package com.uniezz.qa.web;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.uniezz.qa.core.config.Config;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Paths;

/**
 * One browser per test class, a fresh context (clean cookies/storage) per test.
 * A Playwright trace is saved for every test to target/traces (open with `playwright show-trace`).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseWebTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Config.getBoolean("web.headless", true));
        browser = switch (Config.get("web.browser", "chromium")) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }

    @BeforeEach
    void createPage() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setBaseURL(Config.get("web.base.url")));
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
        page = context.newPage();
    }

    @AfterEach
    void closePage(TestInfo info) {
        String name = info.getTestClass().map(Class::getSimpleName).orElse("test")
                + "-" + info.getTestMethod().map(m -> m.getName()).orElse("unknown");
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("target/traces/" + name + ".zip")));
        context.close();
    }

    @AfterAll
    void closeBrowser() {
        browser.close();
        playwright.close();
    }
}
