package org.andrelucs.examplerestapi.integrationtests.controller.withjson;

import io.restassured.RestAssured;
import org.andrelucs.examplerestapi.config.TestConfig;
import org.andrelucs.examplerestapi.integrationtests.dto.UserLoginDTO;
import org.andrelucs.examplerestapi.integrationtests.testcontainers.AbstractIntegrationTest;
import org.andrelucs.examplerestapi.model.dto.TokenDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static TokenDTO dto;

    @Test
    @Order(1)
    public void testLogin(){
        UserLoginDTO user = new UserLoginDTO("leandro", "admin123");

        dto = RestAssured.given()
                .basePath("/auth/login")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().as(TokenDTO.class);

        assertNotNull(dto);
        assertNotNull(dto.getAccessToken());
        assertNotNull(dto.getRefreshToken());

    }
    @Test
    @Order(2)
    public void testRefresh(){
        UserLoginDTO user = new UserLoginDTO("leandro", "admin123");

        TokenDTO refreshedToken = RestAssured.given()
                .basePath("/auth/refresh")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("username", user.getUsername())
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + dto.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract().body().as(TokenDTO.class);

        assertNotNull(refreshedToken);
        assertNotNull(refreshedToken.getAccessToken());
        assertNotNull(refreshedToken.getRefreshToken());

    }

}
