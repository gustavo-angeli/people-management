package com.gusta.backend.controller;

import com.gusta.backend.dto.PersonDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port + "/api/person";
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    //create tests
    @Test
    public void should_Return_Error_Message_When_Empty_Body_CREATE() {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .post()
                .then()
                .body(equalTo("Empty or Invalid request body"));
    }
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    public void should_Return_Error_Message_When_GivenNameIsInvalid(String value) {
        PersonDTO personDTO = new PersonDTO(null, value, "email@email.com", "12345678");
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body(containsString("Null or empty username"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "john.doe@example.com"})
    public void should_Return_Error_Message_When_Given_Email_Is_Invalid_CREATE(String value) {
        PersonDTO personDTO = new PersonDTO(null, "personName", value, "12345678");
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body(
                        anyOf(
                                containsString("Null or empty email address"),
                                containsString("This email already been used")
                        )
                );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "1234567"})
    public void should_Return_Error_Message_When_Given_Password_Is_Invalid_CREATE(String value) {
        PersonDTO personDTO = new PersonDTO(null, "personName", "email@email.com", value);
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body(
                        anyOf(
                                containsString("Password must be at least 8 characters long"),
                                containsString("Null or empty password")
                        )
                );
    }

    @Test
    public void should_Return_Created_Person_When_Valid_CREATE() {
        PersonDTO personDTO = new PersonDTO(null, "personName", "email@email.com", "password123");
        String personDTOJson = "{\"id\":100,\"name\":\"personName\",\"email\":\"email@email.com\",\"password\":\"password123\"}";
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body(equalTo(personDTOJson));
    }
    //create Tests

    //findById tests
    @ParameterizedTest
    @ValueSource(strings = {"NaN", "number"})
    public void should_Return_Error_Message_When_Id_Is_Not_A_Number(String value) {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/" + value)
                .then()
                .statusCode(400)
                .body(equalTo("Given Id is not a number"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "1000"})
    public void should_Return_Error_Message_When_Person_Non_Exists_By_Given_Id(String value) {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/" + value)
                .then()
                .statusCode(400)
                .body(equalTo("Nonexistent person"));
    }

    @Test
    public void should_Return_PersonDTO_When_Valid_Given_Id() {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/1")
                .then()
                .statusCode(200)
                .body(equalTo("{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\"}"));
    }
    //findById tests

    //FindAll tests
    @ParameterizedTest
    @ValueSource(strings = {"0", "number"})
    public void should_Return_Error_Message_When_Invalid_Page_Size(String value) {
        RestAssured.given()
                .contentType("application/json")
                .queryParam("page", "0")
                .queryParam("size", value)
                .when()
                .get()
                .then()
                .body(
                        anyOf(
                                containsString("Given page number / page size is not a number"),
                                containsString("Page size must not be less than one")
                        )
                );
    }
    @ParameterizedTest
    @ValueSource(strings = {"-1", "number"})
    public void should_Return_Error_Message_When_Invalid_Page_Number(String value) {
        RestAssured.given()
                .contentType("application/json")
                .queryParam("page", value)
                .queryParam("size", "4")
                .when()
                .get()
                .then()
                .body(
                        anyOf(
                                containsString("Given page number / page size is not a number"),
                                containsString("Page index must not be less than zero")
                        )
                );
    }
    @Test
    public void should_Return_PersonList_When_Empty_Query_Params() {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .get()
                .then()
                .body(equalTo("[{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\"},{\"id\":2,\"name\":\"personName\",\"email\":\"person@email.com\",\"password\":\"password123\"},{\"id\":3,\"name\":\"Alice Johnson\",\"email\":\"alice.johnson@example.com\",\"password\":\"password789\"},{\"id\":4,\"name\":\"Michael Brown\",\"email\":\"michael.brown@example.com\",\"password\":\"password101\"},{\"id\":5,\"name\":\"Emily Davis\",\"email\":\"emily.davis@example.com\",\"password\":\"password102\"},{\"id\":6,\"name\":\"Christopher Wilson\",\"email\":\"christopher.wilson@example.com\",\"password\":\"password103\"},{\"id\":7,\"name\":\"Sarah Taylor\",\"email\":\"sarah.taylor@example.com\",\"password\":\"password104\"},{\"id\":8,\"name\":\"David Moore\",\"email\":\"david.moore@example.com\",\"password\":\"password105\"},{\"id\":9,\"name\":\"Laura Anderson\",\"email\":\"laura.anderson@example.com\",\"password\":\"password106\"},{\"id\":10,\"name\":\"Daniel Thomas\",\"email\":\"daniel.thomas@example.com\",\"password\":\"password107\"}]"));
    }

    @Test
    public void should_Return_PersonList_When_Valid_Page_Settings() {
        RestAssured.given()
                .contentType("application/json")
                .queryParam("page", "1")
                .queryParam("size", "4")
                .when()
                .get()
                .then()
                .body(equalTo("[{\"id\":5,\"name\":\"Emily Davis\",\"email\":\"emily.davis@example.com\",\"password\":\"password102\"},{\"id\":6,\"name\":\"Christopher Wilson\",\"email\":\"christopher.wilson@example.com\",\"password\":\"password103\"},{\"id\":7,\"name\":\"Sarah Taylor\",\"email\":\"sarah.taylor@example.com\",\"password\":\"password104\"},{\"id\":8,\"name\":\"David Moore\",\"email\":\"david.moore@example.com\",\"password\":\"password105\"}]"));
    }
    //FindAll tests

    //updateById tests
    @Test
    public void should_Return_Error_Message_When_Id_Is_Not_A_Number() {
        PersonDTO personDTO = new PersonDTO(null, "name", "email@email.com", "12345678");
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .put("/number")
                .then()
                .body(equalTo("Given Id is not a number"));
    }
    @Test
    public void should_Return_Error_Message_When_Empty_Body_UPDATE() {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .put("/1")
                .then()
                .body(equalTo("Empty or Invalid request body"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    public void should_Return_Error_Message_When_Invalid_Name_UPDATE(String value) {
        PersonDTO personDTO = new PersonDTO(null, value, "email@email.com", "12345678");
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .put("/2")
                .then()
                .statusCode(400)
                .body(containsString("Null or empty username"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "john.doe@example.com"})
    public void should_Return_Error_Message_When_Given_Email_Is_Invalid_UPDATE(String value) {
        PersonDTO personDTO = new PersonDTO(null, "personName", value, "12345678");
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .put("/2")
                .then()
                .statusCode(400)
                .body(
                        anyOf(
                                containsString("Null or empty email address"),
                                containsString("This email already been used")
                        )
                );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "1234567"})
    public void should_Return_Error_Message_When_Given_Password_Is_Invalid_UPDATE(String value) {
        PersonDTO personDTO = new PersonDTO(null, "personName", "person@email.com", value);
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .put("/2")
                .then()
                .statusCode(400)
                .body(
                        anyOf(
                                containsString("Password must be at least 8 characters long"),
                                containsString("Null or empty password")
                        )
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "1000"})
    public void should_Return_Error_Message_When_Person_Non_Exists_By_Given_Id_UPDATE(String value) {
        PersonDTO personDTO = new PersonDTO(null, "personName", "person@email.com", "password123");
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .put("/" + value)
                .then()
                .statusCode(400)
                .body(equalTo("Nonexistent person"));
    }

    @Test
    public void should_Return_Updated_Person_When_Valid() {
        PersonDTO personDTO = new PersonDTO(null, "personName", "person@email.com", "password123");
        RestAssured.given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .put("/2")
                .then()
                .statusCode(204);
    }
    //updateById tests

    //deleteById tests
    @Test
    public void should_Return_Error_Message_When_Id_Is_Not_A_Number_DELETE() {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/number")
                .then()
                .statusCode(400)
                .body(equalTo("Given Id is not a number"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"-1", "1000"})
    public void should_Return_Error_Message_When_Person_Non_Exists_By_Id_DELETE(String value) {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .delete(value)
                .then()
                .statusCode(400)
                .body(equalTo("Nonexistent person"));
    }
    @Test
    public void should_Return_No_Content_When_Person_Deleted_By_Id() {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/99")
                .then()
                .statusCode(204);
    }
    //deleteById tests

}

