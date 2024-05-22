package com.example.otel.opentelemetrykotlin.events

import java.time.LocalDateTime
import java.util.UUID

data class DomainEventId(val id: UUID)

interface DomainEvent {
    val eventId: DomainEventId
    val createdDate: LocalDateTime
    val updatedDate: LocalDateTime
}

fun interface EventPublisher {
    fun publishEvents(vararg events: DomainEvent)
}

fun interface EventListener {
    fun processEvent(event: DomainEvent)
}
