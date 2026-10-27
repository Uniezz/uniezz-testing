package com.uniezz.qa.core;

/**
 * JUnit 5 tag names shared by all modules. Select them in CI with -Dgroups=smoke.
 */
public final class TestTags {

    public static final String SMOKE = "smoke";
    public static final String REGRESSION = "regression";

    private TestTags() {
    }
}
