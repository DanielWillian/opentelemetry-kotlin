package com.example.otel.opentelemetrykotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class OpentelemetryKotlinApplication

fun main(args: Array<String>) {
  runApplication<OpentelemetryKotlinApplication>(*args)
}
