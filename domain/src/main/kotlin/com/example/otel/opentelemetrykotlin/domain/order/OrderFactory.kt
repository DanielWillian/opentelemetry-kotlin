package com.example.otel.opentelemetrykotlin.domain.order

import com.example.otel.opentelemetrykotlin.domain.events.DomainEventId
import java.time.LocalDateTime
import java.util.UUID

fun interface OrderFactory {
  fun createOrder(products: List<Pair<Product, Int>>): OrderCreatedResult
}

data class OrderCreatedResult(val order: Order, val events: List<OrderItemCreated>)

fun Order.toOrderCreatedResult(): OrderCreatedResult {
  val now = LocalDateTime.now()
  val events =
      orderItems.map {
        OrderItemCreated(
            eventId = DomainEventId(UUID.randomUUID()),
            createdDate = now,
            updatedDate = now,
            itemId = it.id)
      }
  return OrderCreatedResult(order = this, events = events)
}

class OrderFactoryImpl : OrderFactory {
  override fun createOrder(products: List<Pair<Product, Int>>): OrderCreatedResult =
      Order(
              id = OrderId(UUID.randomUUID()),
              status = OrderStatus.CREATED,
              orderItems = products.map(::createOrderItem))
          .toOrderCreatedResult()

  private fun createOrderItem(product: Pair<Product, Int>): OrderItem =
      OrderItem(
          id = OrderItemId(UUID.randomUUID()),
          status = OrderItemStatus.CREATED,
          product = product.first,
          quantity = product.second)
}
