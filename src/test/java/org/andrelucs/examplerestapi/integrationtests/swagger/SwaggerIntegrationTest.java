package org.andrelucs.examplerestapi.integrationtests.swagger;

import io.restassured.RestAssured;
import org.andrelucs.examplerestapi.config.TestConfig;
import org.andrelucs.examplerestapi.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void testSwaggerEndpoint() {
        var bodyContent = RestAssured.given()
                .basePath("/swagger-ui/index.html")
                .port(TestConfig.SERVER_PORT)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body().asString();

        assertTrue(bodyContent.contains("Swagger UI"));
    }
}
