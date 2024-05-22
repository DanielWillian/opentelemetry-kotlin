package com.example.otel.opentelemetrykotlin.order

import java.util.UUID

data class OrderId(val id: UUID)

enum class OrderStatus {
    CREATED,
    READY_TO_DELIVER,
    DELIVERED
}

data class OrderItemId(val id: UUID)

enum class OrderItemStatus {
    CREATED,
    READY,
    DELIVERED
}

enum class Product {
    APPLE,
    BANANA,
    STRAWBERRY,
    ORANGE
}

data class OrderItem(
    val id: OrderItemId,
    val status: OrderItemStatus,
    val product: Product,
    val quantity: Int
)

class IllegalOrderOperation(message: String) : RuntimeException(message)

data class Order(val id: OrderId, val status: OrderStatus, val orderItems: List<OrderItem>) {
    fun changeItemToReady(orderItemId: OrderItemId): Order {
        val orderItem =
            orderItems.find { it.id == orderItemId }
                ?: throw IllegalOrderOperation("Item ${orderItemId.id} not found in order ${id.id}")

        if (orderItem.status != OrderItemStatus.CREATED) {
            throw IllegalOrderOperation(
                "Item ${orderItem.id.id} can not be changed to ready because it is not " +
                    "in CREATED status. It is ${orderItem.status}")
        }

        val readyItem = orderItem.copy(status = OrderItemStatus.READY)
        val updatedOrderItems = orderItems.map { if (it == orderItem) readyItem else it }
        val updatedOrder = copy(orderItems = updatedOrderItems)
        return if (updatedOrder.shouldOrderBecomeReadyToDeliver()) {
            updatedOrder.copy(status = OrderStatus.READY_TO_DELIVER)
        } else {
            updatedOrder
        }
    }

    private fun shouldOrderBecomeReadyToDeliver(): Boolean =
        status == OrderStatus.CREATED &&
            orderItems.stream().allMatch { it.status == OrderItemStatus.READY }
}
