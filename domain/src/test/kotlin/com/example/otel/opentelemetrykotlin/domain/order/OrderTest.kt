package com.example.otel.opentelemetrykotlin.domain.order

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

private val orderItemId = OrderItemId(UUID.fromString("a11636af-7082-47c3-b3e1-844674f9838e"))
private val orderItemId2 = OrderItemId(UUID.fromString("d2299d38-3ab2-4e73-8218-540350f9fc31"))
private val orderId = OrderId(UUID.fromString("d8484cbc-baa2-49c2-a0c5-396ffa1a8897"))

fun createOrder() = Order(orderId, OrderStatus.CREATED, listOf(createOrderItem()))

fun createOrder(orderItems: List<OrderItem>) = Order(orderId, OrderStatus.CREATED, orderItems)

fun createOrderItem() = OrderItem(orderItemId, OrderItemStatus.CREATED, Product.APPLE, 1)

class OrderTest :
    FunSpec({
      test("Change non existing order item to READY") {
        val nonExisting = OrderItemId(UUID.fromString("9f993cd3-7f1c-4efd-99b1-44c54d80f2d6"))
        val order = createOrder()
        shouldThrow<IllegalOrderOperation> { order.changeItemToReady(nonExisting) }
      }

      listOf(OrderItemStatus.READY, OrderItemStatus.DELIVERED).forEach {
        test("Change order item with status $it to READY") {
          val orderItem = createOrderItem().copy(status = it)
          val order = createOrder(listOf(orderItem))
          shouldThrow<IllegalOrderOperation> { order.changeItemToReady(orderItemId) }
        }
      }

      context("Order with one order item and changing it to READY") {
        val order = createOrder()
        val changedOrder = order.changeItemToReady(orderItemId)

        test("order item should be READY") {
          changedOrder.orderItems.find { it.id == orderItemId }?.status shouldBe
              OrderItemStatus.READY
        }

        test("order should be READY") { changedOrder.status shouldBe OrderStatus.READY_TO_DELIVER }
      }

      context("Order with two order items and changing both to READY") {
        val orderItem = createOrderItem()
        val orderItem2 = createOrderItem().copy(id = orderItemId2, product = Product.BANANA)
        val order = createOrder(listOf(orderItem, orderItem2))
        context("Changing order item one to READY") {
          val changedOrder = order.changeItemToReady(orderItemId)

          test("order item one should be READY") {
            changedOrder.orderItems.find { it.id == orderItemId }?.status shouldBe
                OrderItemStatus.READY
          }

          test("order item two should be CREATED") {
            changedOrder.orderItems.find { it.id == orderItemId2 }?.status shouldBe
                OrderItemStatus.CREATED
          }

          test("order should be CREATED") { changedOrder.status shouldBe OrderStatus.CREATED }
          context("Changing order item two to READY") {
            val changedOrder2 = changedOrder.changeItemToReady(orderItemId2)

            test("order item one should be READY") {
              changedOrder2.orderItems.find { it.id == orderItemId }?.status shouldBe
                  OrderItemStatus.READY
            }

            test("order item two should be READY") {
              changedOrder2.orderItems.find { it.id == orderItemId2 }?.status shouldBe
                  OrderItemStatus.READY
            }

            test("order should be READY") {
              changedOrder2.status shouldBe OrderStatus.READY_TO_DELIVER
            }
          }
        }
      }
    })
