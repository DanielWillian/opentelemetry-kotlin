package com.example.otel.opentelemetrykotlin

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.time.Duration.Companion.seconds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.core.env.Environment
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

fun JsonNode.getText(field: String): String? = get(field)?.asText()

fun JsonNode.getInt(field: String): Int? = get(field)?.asInt()

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderIntegrationTest : FunSpec() {
  @Autowired lateinit var environment: Environment
  @Autowired lateinit var objectMapper: ObjectMapper

  override fun extensions() = listOf(SpringExtension)

  init {
    postgres.start()
    kafka.start()
    context("Create order and eventually get it ready for delivery") {
      val port = environment.getProperty("local.server.port")
      val baseUrl = "http://localhost:$port"
      context("Create order") {
        val body = "{ \"products\": [ { \"product\": \"STRAWBERRY\", \"quantity\":1 } ] }"
        val httpRequest =
            HttpRequest.newBuilder()
                .uri(URI.create("$baseUrl/orders/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build()
        val httpClient = HttpClient.newHttpClient()
        val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
        test("Response status is 201") { response.statusCode() shouldBe 201 }
        val json = objectMapper.readTree(response.body())
        val orderId = json.getText("id")
        val location = response.headers().firstValue("Location").get()
        test("Location points to the order id") { location shouldBe "/orders/$orderId" }
        test("Order is CREATED") { json.getText("status") shouldBe "CREATED" }
        val orderItems = json.get("orderItems")
        test("Order has 1 order item") { orderItems.size() shouldBe 1 }
        val orderItem = orderItems.get(0)
        test("Order item is CREATED") { orderItem.getText("status") shouldBe "CREATED" }
        test("Order item is of Strawberry") { orderItem.getText("product") shouldBe "STRAWBERRY" }
        test("Order item is for 1 quantity") { orderItem.getInt("quantity") shouldBe 1 }
        context("Eventually get it ready") {
          val getRequest =
              HttpRequest.newBuilder().uri(URI.create("$baseUrl$location")).GET().build()
          eventually(5.seconds) {
            val getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString())
            val getJson = objectMapper.readTree(getResponse.body())
            getJson.getText("status") shouldBe "READY_TO_DELIVER"
          }
          val getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString())
          test("Response status is 200") { getResponse.statusCode() shouldBe 200 }
          val getJson = objectMapper.readTree(getResponse.body())
          test("Order is ready to deliver") {
            getJson.getText("status") shouldBe "READY_TO_DELIVER"
          }
          val getOrderItems = getJson.get("orderItems")
          test("Order has 1 order item") { getOrderItems.size() shouldBe 1 }
          val getOrderItem = getOrderItems.get(0)
          test("Order item is ready") { getOrderItem.getText("status") shouldBe "READY" }
        }
      }
    }
  }

  companion object {
    @Container @ServiceConnection val postgres = PostgreSQLContainer("postgres:latest")

    @Container
    @ServiceConnection
    val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.3"))
  }
}
