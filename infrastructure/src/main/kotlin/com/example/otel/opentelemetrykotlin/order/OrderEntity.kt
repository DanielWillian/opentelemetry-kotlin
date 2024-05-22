package com.example.otel.opentelemetrykotlin.order

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

fun Order.toOrderEntity(): OrderEntity =
    OrderEntity(id = id.id, status = status, orderItems = orderItems.map { it.toOrderEntity(id) })

fun OrderItem.toOrderEntity(orderId: OrderId): OrderItemEntity =
    OrderItemEntity(
        id = id.id, orderId = orderId.id, status = status, product = product, quantity = quantity)

@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id val id: UUID,
    @Enumerated(EnumType.STRING) val status: OrderStatus,
    @OneToMany(mappedBy = "orderId", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val orderItems: List<OrderItemEntity>
) {
    fun toOrder(): Order =
        Order(id = OrderId(id), status = status, orderItems = orderItems.map { it.toOrderItem() })
}

@Entity
@Table(name = "orderItems")
data class OrderItemEntity(
    @Id val id: UUID,
    val orderId: UUID,
    @Enumerated(EnumType.STRING) val status: OrderItemStatus,
    @Enumerated(EnumType.STRING) val product: Product,
    val quantity: Int
) {
    fun toOrderItem(): OrderItem =
        OrderItem(id = OrderItemId(id), status = status, product = product, quantity = quantity)
}
