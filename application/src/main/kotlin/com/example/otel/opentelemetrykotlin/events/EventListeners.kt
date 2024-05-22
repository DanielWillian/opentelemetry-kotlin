package com.example.otel.opentelemetrykotlin.events

import com.example.otel.opentelemetrykotlin.order.OrderItemCreated
import com.example.otel.opentelemetrykotlin.order.OrderService
import org.springframework.stereotype.Component

@Component
class OrderItemCreatedListener(private val orderService: OrderService) : EventListener {
    override fun processEvent(event: DomainEvent) {
        if (event !is OrderItemCreated) return

        orderService.prepareOrderItem(event.itemId)
    }
}
