package com.nvoulgaris.taskmanager

import io.restassured.response.Response
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static io.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.is

class TaskRetrievalSpec extends Specification {

  private static Logger logger = LoggerFactory.getLogger(TaskRetrievalSpec.class)

  def "Should retrieve all tasks for a specific assignee"() {
    given:
      Response createUserResponse1 = given()
        .body(userJsonWith(randomUsername(), "123"))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/users")
      
      String userId1 = createUserResponse1.path("id")
      logger.info("Created user 1 with ID $userId1")

      Response createUserResponse2 = given()
        .body(userJsonWith(randomUsername(), "123"))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/users")
      
      String userId2 = createUserResponse2.path("id")
      logger.info("Created user 2 with ID $userId2")

    and:
      // Create task 1 for user 1
      given()
        .body(taskJsonWith("Clean apartment", "TODO", userId1, false))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/tasks")
        .then()
        .statusCode(201)

      // Create task 2 for user 1
      given()
        .body(taskJsonWith("Buy groceries", "TODO", userId1, false))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/tasks")
        .then()
        .statusCode(201)

      // Create task 3 for user 2
      given()
        .body(taskJsonWith("Read a book", "TODO", userId2, false))
        .contentType(JSON)
        .when()
        .post("http://localhost:8080/api/tasks")
        .then()
        .statusCode(201)

    expect:
      given()
        .contentType(JSON)
        .queryParam("assigneeId", userId1)
        .when()
        .get("http://localhost:8080/api/tasks")
        .then()
        .statusCode(200)
        .contentType(JSON)
        .body("size()", is(2))
        .body("title", containsInAnyOrder("Clean apartment", "Buy groceries"))
        .body("assigneeId", containsInAnyOrder(userId1, userId1))
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
