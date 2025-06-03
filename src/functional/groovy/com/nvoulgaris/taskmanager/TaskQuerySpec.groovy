package com.nvoulgaris.taskmanager

import io.restassured.response.Response
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static io.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.*

class TaskQuerySpec extends Specification {

  private static Logger logger = LoggerFactory.getLogger(TaskQuerySpec.class)

  def "Should get all tasks for a user"() {
    given:
      // Register a user
      Response userResponse = given()
        .body(userJsonWith(randomUsername(), "123"))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/users")

      String assigneeId = userResponse.path("id")
      logger.info("Created user with ID $assigneeId")

    and:
      // Create two tasks for this user
      Response task1Response = given()
        .body(taskJsonWith("Task 1", "TODO", assigneeId, false))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/tasks")
      String task1Id = task1Response.path("id")

      Response task2Response = given()
        .body(taskJsonWith("Task 2", "IN_PROGRESS", assigneeId, false))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/tasks")
      String task2Id = task2Response.path("id")

    expect:
      Response getResponse = given()
        .contentType(JSON)
        .when()
        .get("http://localhost:8080/api/tasks?assigneeId=${assigneeId}")

      getResponse.then()
        .statusCode(200)
        .contentType(JSON)
        .body("size()", is(2))
        .body("id", hasItems(task1Id, task2Id))
        .body("title", hasItems("Task 1", "Task 2"))
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