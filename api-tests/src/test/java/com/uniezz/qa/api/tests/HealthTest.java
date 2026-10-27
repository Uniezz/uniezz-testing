package com.uniezz.qa.api.tests;

import com.uniezz.qa.api.client.HealthClient;
import com.uniezz.qa.core.TestTags;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag(TestTags.SMOKE)
class HealthTest {

    private final HealthClient health = new HealthClient();

    @Test
    void healthEndpointReturns200() {
        assertThat(health.getHealth().statusCode()).isEqualTo(200);
    }
}
