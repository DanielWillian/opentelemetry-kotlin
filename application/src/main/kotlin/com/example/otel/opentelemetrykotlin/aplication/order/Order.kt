package com.example.otel.opentelemetrykotlin.aplication.order

import com.example.otel.opentelemetrykotlin.domain.order.Order
import com.example.otel.opentelemetrykotlin.domain.order.OrderItem
import com.example.otel.opentelemetrykotlin.domain.order.OrderItemStatus
import com.example.otel.opentelemetrykotlin.domain.order.OrderStatus
import com.example.otel.opentelemetrykotlin.domain.order.Product
import java.util.UUID

data class ProductRequest(val product: Product, val quantity: Int) {
  fun toProductPair(): Pair<Product, Int> = Pair(product, quantity)
}

data class CreateOrderRequest(val products: List<ProductRequest>)

fun Order.toOrderResponse(): OrderResponse =
    OrderResponse(
        id = id.id, status = status, orderItems = orderItems.map { it.toOrderItemResponse() })

fun OrderItem.toOrderItemResponse(): OrderItemResponse =
    OrderItemResponse(id = id.id, status = status, product = product, quantity = quantity)

data class OrderResponse(
    val id: UUID,
    val status: OrderStatus,
    val orderItems: List<OrderItemResponse>
)

data class OrderItemResponse(
    val id: UUID,
    val status: OrderItemStatus,
    val product: Product,
    val quantity: Int
)
