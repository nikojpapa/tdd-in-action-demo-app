package com.nvoulgaris.taskmanager

import org.json.JSONObject
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static io.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.matchesPattern

class UserRegistrationSpec extends Specification {

  static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

  def "Should register a new user"() {
    expect:
      given()
        .body(jsonWith("Alice", "123"))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/users")
        .then()
        .statusCode(201)
        .contentType(JSON)
        .body("id", matchesPattern(UUID_PATTERN))
        .body("username", is("Alice"))
  }

  String jsonWith(String username, String password) {
    return new JSONObject()
      .put("username", username)
      .put("password", password)
    toString()
  }
}
