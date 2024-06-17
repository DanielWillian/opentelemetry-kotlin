package com.example.otel.opentelemetrykotlin.aplication.events

import com.example.otel.opentelemetrykotlin.domain.events.DomainEvent
import com.example.otel.opentelemetrykotlin.domain.events.EventListener
import com.example.otel.opentelemetrykotlin.domain.order.OrderItemCreated
import com.example.otel.opentelemetrykotlin.domain.order.OrderService
import org.springframework.stereotype.Component

@Component
class OrderItemCreatedListener(private val orderService: OrderService) : EventListener {
  override fun processEvent(event: DomainEvent) {
    if (event !is OrderItemCreated) return

    orderService.prepareOrderItem(event.itemId)
  }
}
