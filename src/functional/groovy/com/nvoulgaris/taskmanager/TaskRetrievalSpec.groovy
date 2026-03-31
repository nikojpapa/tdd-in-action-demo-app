package com.nvoulgaris.taskmanager

import io.restassured.response.Response
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static io.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.hasSize

class TaskRetrievalSpec extends Specification {

    private static Logger logger = LoggerFactory.getLogger(TaskRetrievalSpec.class)
    static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

    def "Should retrieve all tasks for a given assignee"() {
        given:
            // create a user
            Response userResponse = given()
                .body(userJsonWith(randomUsername(), "123"))
                .contentType(JSON)
                .when()
                .post("http://localhost:8080/api/users")
            String userId = userResponse.path("id")
            logger.info("Created user with ID $userId")

            // create a task assigned to that user
            Response taskResponse = given()
                .body(taskJsonWith("Clean apartment", "TODO", userId, false))
                .contentType(JSON)
                .when()
                .post("http://localhost:8080/api/tasks")
            String taskId = taskResponse.path("id")
            logger.info("Created task with ID $taskId")

        expect:
            // retrieve tasks for the assignee
            given()
                .queryParam("assigneeId", userId)
                .when()
                .get("http://localhost:8080/api/tasks")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", is(1))
                .body("[0].id", is(taskId))
                .body("[0].title", is("Clean apartment"))
                .body("[0].assigneeId", is(userId))
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
