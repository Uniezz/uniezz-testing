package com.uniezz.qa.api.client;

import com.uniezz.qa.api.specs.Specs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * One client per API resource keeps endpoint paths out of the tests.
 */
public class HealthClient {

    public Response getHealth() {
        return given()
                .spec(Specs.base())
                .when()
                .get("/health");
    }
}
