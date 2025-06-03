package com.nvoulgaris.taskmanager

import io.restassured.response.Response
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static io.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.matchesPattern

class TaskCreationSpec extends Specification {

  private static Logger logger = LoggerFactory.getLogger(TaskCreationSpec.class)
  static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

  def "Should create a new task"() {
    given:
      Response response = given()
        .body(userJsonWith(randomUsername(), "123"))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/users")

      String id = response.path("id")
      logger.info("Created user with ID $id")

    expect:
      given()
        .body(taskJsonWith("Clean apartment", "TODO", id, false))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/tasks")
        .then()
        .statusCode(201)
        .contentType(JSON)
        .body("id", matchesPattern(UUID_PATTERN))
        .body("title", is("Clean apartment"))
  }

  String userJsonWith(String username, String password) {
    return new JSONObject()
      .put("username", username)
      .put("password", password)
      .toString()
  }

  String taskJsonWith(String title, String status, String assigneeId, boolean blocked) {
    return new JSONObject()
      .put("title", title)
      .put("status", status)
      .put("assigneeId", assigneeId)
      .put("blocked", blocked)
      .toString()
  }

  String randomUsername() {
    new Random().with { (1..9).collect { (('a'..'z')).join()[nextInt((('a'..'z')).join().length())] }.join() }
  }
}
