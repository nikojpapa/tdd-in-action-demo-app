package com.nvoulgaris.taskmanager

import io.restassured.response.Response
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static io.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.is

class TaskStatusUpdateSpec extends Specification {

  private static Logger logger = LoggerFactory.getLogger(TaskStatusUpdateSpec.class)

  def "Should update the status of a task"() {
    given:
      Response createUserResponse = given()
        .body(userJsonWith(randomUsername(), "123"))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/users")

      String userId = createUserResponse.path("id")
      logger.info("Created user with ID $userId")

    and:
      Response createTaskResponse = given()
        .body(taskJsonWith("Clean apartment", "TODO", userId, false))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/tasks")

      String taskId = createTaskResponse.path("id")
      logger.info("Created task with ID $taskId")

    expect:
      given()
        .body(taskUpdateJsonWith("IN_PROGRESS"))
        .contentType(JSON)
        .when()
        .patch("http://localhost:8080/api/tasks/$taskId")
        .then()
        .statusCode(200)
        .contentType(JSON)
        .body("title", is("Clean apartment"))
        .body("status", is("IN_PROGRESS"))

    and:
      given()
        .body(taskUpdateJsonWith("DONE"))
        .contentType(JSON)
        .when()
        .patch("http://localhost:8080/api/tasks/$taskId")
        .then()
        .statusCode(200)
        .contentType(JSON)
        .body("title", is("Clean apartment"))
        .body("status", is("DONE"))
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

  String taskUpdateJsonWith(String status) {
    return new JSONObject()
      .put("status", status)
      .toString()
  }

  String randomUsername() {
    new Random().with { (1..9).collect { (('a'..'z')).join()[nextInt((('a'..'z')).join().length())] }.join() }
  }
}
