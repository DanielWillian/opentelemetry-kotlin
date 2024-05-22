package com.example.otel.opentelemetrykotlin.events

import com.example.otel.opentelemetrykotlin.order.OrderItemCreated
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class EventSubscribers(
    private val orderItemCreatedListener: OrderItemCreatedListener,
    private val objectMapper: ObjectMapper
) {
    val logger: Logger = LoggerFactory.getLogger(EventPublisherImpl::class.java)

    fun publishDomainEvent(event: DomainEvent) {
        when (event) {
            is OrderItemCreated -> orderItemCreatedListener.processEvent(event)
            else -> throw IllegalArgumentException("Unknown wrapper class: ${event::class}")
        }
    }

    @KafkaListener(topics = [EVENT_TOPIC_NAME], groupId = GROUP_ID)
    fun listenEvents(message: String) {
        logger.info("Processing message: $message")
        val wrapper = objectMapper.readValue(message, EventWrapper::class.java)
        publishDomainEvent(createEvent(wrapper))
    }
}

@Service
class EventPublisherImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : EventPublisher {
    val logger: Logger = LoggerFactory.getLogger(EventPublisherImpl::class.java)

    override fun publishEvents(vararg events: DomainEvent) {
        for (event in events) {
            val wrapper = createWrapper(event)
            logger.info("Publishing event: $wrapper")
            val message = objectMapper.writeValueAsString(wrapper)
            kafkaTemplate.send(EVENT_TOPIC_NAME, message)
        }
    }
}
