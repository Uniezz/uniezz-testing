package com.uniezz.qa.web.pages;

import com.microsoft.playwright.Page;

/**
 * Page Object: locators and actions for one page; tests never touch selectors directly.
 */
public class HomePage {

    private final Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public HomePage open() {
        page.navigate("/");
        return this;
    }

    public String title() {
        return page.title();
    }
}
