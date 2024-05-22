package com.example.otel.opentelemetrykotlin.order

import com.example.otel.opentelemetrykotlin.events.DomainEvent
import com.example.otel.opentelemetrykotlin.events.DomainEventId
import java.time.LocalDateTime

data class OrderItemCreated(
    override val eventId: DomainEventId,
    override val createdDate: LocalDateTime,
    override val updatedDate: LocalDateTime,
    val itemId: OrderItemId
) : DomainEvent
