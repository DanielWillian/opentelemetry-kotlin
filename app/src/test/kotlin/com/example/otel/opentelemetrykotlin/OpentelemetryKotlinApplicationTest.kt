package com.example.otel.opentelemetrykotlin

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest
class OpentelemetryKotlinApplicationTest {
  @Test fun loadContext() {}

  companion object {
    @Container @ServiceConnection val postgres = PostgreSQLContainer("postgres:latest")

    @Container
    @ServiceConnection
    val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.3"))
  }
}
