package com.example.otel.opentelemetrykotlin.infrastructure.events

import com.example.otel.opentelemetrykotlin.domain.events.DomainEvent
import com.example.otel.opentelemetrykotlin.domain.events.DomainEventId
import com.example.otel.opentelemetrykotlin.domain.order.OrderItemCreated
import com.example.otel.opentelemetrykotlin.domain.order.OrderItemId
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime

const val EVENT_TOPIC_NAME = "events"
const val GROUP_ID = "groupId"

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
interface EventWrapper {
  val headers: Map<String, String>
}

data class OrderItemCreatedWrapper(
    val eventId: DomainEventId,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime,
    val itemId: OrderItemId,
    override val headers: Map<String, String>
) : EventWrapper {
  fun toEvent(): OrderItemCreated =
      OrderItemCreated(
          eventId = eventId, createdDate = createdDate, updatedDate = updatedDate, itemId = itemId)
}

fun OrderItemCreated.toEventWrapper(headers: Map<String, String>): OrderItemCreatedWrapper =
    OrderItemCreatedWrapper(
        eventId = eventId,
        createdDate = createdDate,
        updatedDate = updatedDate,
        itemId = itemId,
        headers = headers)

fun createWrapper(event: DomainEvent): EventWrapper =
    when (event) {
      is OrderItemCreated -> event.toEventWrapper(mapOf())
      else -> throw IllegalArgumentException("Unknown event class: ${event::class}")
    }

fun createEvent(wrapper: EventWrapper): DomainEvent =
    when (wrapper) {
      is OrderItemCreatedWrapper -> wrapper.toEvent()
      else -> throw IllegalArgumentException("Unknown wrapper class: ${wrapper::class}")
    }
