package org.andrelucs.examplerestapi.integrationtests.controller.withjson;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.andrelucs.examplerestapi.config.TestConfig;
import org.andrelucs.examplerestapi.integrationtests.dto.PersonDTO;
import org.andrelucs.examplerestapi.integrationtests.dto.UserLoginDTO;
import org.andrelucs.examplerestapi.integrationtests.testcontainers.AbstractIntegrationTest;
import org.andrelucs.examplerestapi.model.dto.TokenDTO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;

    private static PersonDTO dto;

    @BeforeAll
    public static void setUp(){
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        dto = new PersonDTO();
    }
    @Test
    @Order(0)
    public void testUnauthorized(){
        RestAssured.given()
                .basePath("/api/person")
                .port(TestConfig.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(403);
    }
    @Test
    @Order(1)
    public void testGetToken(){
        UserLoginDTO user = new UserLoginDTO("leandro", "admin123");

        var accessToken = RestAssured.given()
                .basePath("/auth/login")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                    .post()
                    .then()
                .statusCode(200)
                .extract().body().as(TokenDTO.class).getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }



    @Test
    @Order(2)
    public void testInvalidOrigin(){
        var body = RestAssured.given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.INVALID_TEST_ORIGIN)
                .when()
                   .get()
                .then()
                   .statusCode(403)
                .extract()
                .body().asString();

        assertNotNull(body);
        assertTrue(body.contains("Invalid CORS request"));
    }

    @Test
    @Order(3)
    public void testCreate() throws IOException {
        mockPerson();
        dto.setKey(null);
        var bodyContent = RestAssured.given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.VALID_TEST_ORIGIN)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(dto)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body().asString();

        dto = mapper.readValue(bodyContent, PersonDTO.class);

        assertNotNull(dto);
        assertNotNull(dto.getKey());
        assertNotNull(dto.getAddress());
        assertNotNull(dto.getGender());
        assertNotNull(dto.getFirstName());
        assertNotNull(dto.getLastName());

        assertTrue(dto.getKey()>0);

        assertEquals(dto.getFirstName(), "John");
        assertEquals(dto.getLastName(), "Doe");
        assertEquals(dto.getAddress(), "123 Main St");
        assertEquals(dto.getGender(), "Male");
    }
    @Test
    @Order(4)
    public void testFindById() throws IOException {
        var responseBody = RestAssured.given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.VALID_TEST_ORIGIN)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 1L)
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body().asString();

        var res = mapper.readValue(responseBody, PersonDTO.class);
        dto = res;
        assertNotNull(res);
        assertNotNull(res.getKey());
        assertNotNull(res.getAddress());
        assertNotNull(res.getGender());
        assertNotNull(res.getFirstName());
        assertNotNull(res.getLastName());

        assertTrue(res.getKey()>0);

        assertEquals("Ayrton", res.getFirstName());
        assertEquals("Senna", res.getLastName());
        assertEquals("São Paulo", res.getAddress());
        assertEquals("Male", res.getGender());
    }

    @Test
    @Order(5)
    public void testUpdate() throws IOException {
        PersonDTO person = new PersonDTO();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAddress(dto.getAddress());
        person.setGender(dto.getGender());

        mockPerson();
        dto.setLastName(person.getLastName());
        dto.setGender("Female");
        var bodyContent = RestAssured.given().spec(specification)
                //.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.VALID_TEST_ORIGIN)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(dto)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .extract()
                    .body().asString();

        dto = mapper.readValue(bodyContent, PersonDTO.class);

        assertNotNull(dto);
        assertNotNull(dto.getKey());
        assertNotNull(dto.getAddress());
        assertNotNull(dto.getGender());
        assertNotNull(dto.getFirstName());
        assertNotNull(dto.getLastName());

        assertTrue(dto.getKey()>0);

        assertEquals(person.getLastName(), dto.getLastName());
        assertNotEquals(person.getFirstName(), dto.getFirstName());
        assertNotEquals(dto.getAddress(), person.getAddress());
        assertNotEquals(dto.getGender(), person.getGender());
    }

    @Test
    @Order(6)
    public void testDelete() {
        RestAssured.given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 1L)
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);

        RestAssured.given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 1L)
                .when()
                    .get("{id}")
                .then()
                    .statusCode(404);
    }

    @Test
    @Order(7)
    public void testDisable() {
        RestAssured.given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 2L)
                .when()
                    .patch("/disable/{id}")
                .then()
                .statusCode(204);

        var body = RestAssured.given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 2L)
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract().body().asString();

        assertNotNull(body);
        System.out.println(body);
        assertTrue(body.contains("\"enabled\":false"));
    }

    private void mockPerson() {
        dto.setKey(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("123 Main St");
        dto.setGender("Male");
        dto.setEnabled(true);
    }
}
