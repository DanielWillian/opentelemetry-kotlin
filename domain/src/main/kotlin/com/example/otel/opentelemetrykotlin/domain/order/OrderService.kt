package com.example.otel.opentelemetrykotlin.domain.order

interface OrderService {
  fun createOrder(products: List<Pair<Product, Int>>): Order

  fun getOrder(id: OrderId): Order?

  fun prepareOrderItem(itemId: OrderItemId)
}

interface OrderRepository {
  fun saveOrder(order: Order)

  fun getOrder(orderId: OrderId): Order?

  fun getOrder(orderItemId: OrderItemId): Order?
}
