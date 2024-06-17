package com.example.otel.opentelemetrykotlin.aplication.order

import com.example.otel.opentelemetrykotlin.domain.events.EventPublisher
import com.example.otel.opentelemetrykotlin.domain.order.IllegalOrderOperation
import com.example.otel.opentelemetrykotlin.domain.order.Order
import com.example.otel.opentelemetrykotlin.domain.order.OrderFactory
import com.example.otel.opentelemetrykotlin.domain.order.OrderId
import com.example.otel.opentelemetrykotlin.domain.order.OrderItemId
import com.example.otel.opentelemetrykotlin.domain.order.OrderRepository
import com.example.otel.opentelemetrykotlin.domain.order.OrderService
import com.example.otel.opentelemetrykotlin.domain.order.Product
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderFactory: OrderFactory,
    private val eventPublisher: EventPublisher
) : OrderService {
  override fun createOrder(products: List<Pair<Product, Int>>): Order {
    val result = orderFactory.createOrder(products)
    orderRepository.saveOrder(result.order)
    eventPublisher.publishEvents(*result.events.toTypedArray())
    return result.order
  }

  override fun getOrder(id: OrderId): Order? = orderRepository.getOrder(id)

  override fun prepareOrderItem(itemId: OrderItemId) {
    val order =
        orderRepository.getOrder(itemId)
            ?: throw IllegalStateException("Order id ${itemId.id} does not map to an order")
    try {
      val changedOrder = order.changeItemToReady(itemId)
      orderRepository.saveOrder(changedOrder)
    } catch (e: IllegalOrderOperation) {
      throw IllegalStateException("Could not change order item ${itemId.id} to ready", e)
    }
  }
}
