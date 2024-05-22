package com.example.otel.opentelemetrykotlin.order

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

interface OrderRepositoryJpa : JpaRepository<OrderEntity, UUID> {
    @Query(
        "SELECT o FROM OrderItemEntity i INNER JOIN OrderEntity o ON i.orderId = o.id WHERE i.id = ?1")
    fun findByOrderItemId(orderItemId: UUID): OrderEntity?
}

@Repository
class OrderRepositoryImpl(private val repositoryJpa: OrderRepositoryJpa) : OrderRepository {
    override fun saveOrder(order: Order) {
        repositoryJpa.save(order.toOrderEntity())
    }

    override fun getOrder(orderId: OrderId): Order? =
        repositoryJpa.findById(orderId.id).map { it.toOrder() }.orElse(null)

    override fun getOrder(orderItemId: OrderItemId): Order? =
        repositoryJpa.findByOrderItemId(orderItemId.id).let { it?.toOrder() }
}
