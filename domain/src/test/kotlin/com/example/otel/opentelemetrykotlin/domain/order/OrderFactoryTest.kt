package com.example.otel.opentelemetrykotlin.domain.order

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class OrderFactoryTest :
    FunSpec({
      context("Create order with one product") {
        val products = listOf(Pair(Product.STRAWBERRY, 3))
        val result = OrderFactoryImpl().createOrder(products)
        val order = result.order
        val events = result.events
        test("The order is in CREATED status") { order.status shouldBe OrderStatus.CREATED }
        test("The order has 1 order item") { order.orderItems shouldHaveSize 1 }
        test("1 order items created event is returned") { events shouldHaveSize 1 }
        context("The first order item") {
          val item = order.orderItems[0]
          test("is in CREATED status") { item.status shouldBe OrderItemStatus.CREATED }
          test("is for STRAWBERRY") { item.product shouldBe Product.STRAWBERRY }
          test("has quantity of 1") { item.quantity shouldBe 3 }
          test("has an event for its creation") { events.map { it.itemId } shouldContain item.id }
        }
      }

      context("Create order with two product") {
        val products = listOf(Pair(Product.STRAWBERRY, 1), Pair(Product.BANANA, 2))
        val result = OrderFactoryImpl().createOrder(products)
        val order = result.order
        val events = result.events
        test("The order is in CREATED status") { order.status shouldBe OrderStatus.CREATED }
        test("The order has two order items") { order.orderItems shouldHaveSize 2 }
        test("Two order item created event are returned") { events shouldHaveSize 2 }
        context("The first order item") {
          val item = order.orderItems[0]
          test("is in CREATED status") { item.status shouldBe OrderItemStatus.CREATED }
          test("is for STRAWBERRY") { item.product shouldBe Product.STRAWBERRY }
          test("has quantity of 1") { item.quantity shouldBe 1 }
          test("has an event for its creation") { events.map { it.itemId } shouldContain item.id }
        }
        context("The second order item") {
          val item = order.orderItems[1]
          test("is in CREATED status") { item.status shouldBe OrderItemStatus.CREATED }
          test("is for STRAWBERRY") { item.product shouldBe Product.BANANA }
          test("has quantity of 2") { item.quantity shouldBe 2 }
          test("has an event for its creation") { events.map { it.itemId } shouldContain item.id }
        }
      }

      context("Create order with the same product twice") {
        val products = listOf(Pair(Product.STRAWBERRY, 4), Pair(Product.STRAWBERRY, 5))
        val result = OrderFactoryImpl().createOrder(products)
        val order = result.order
        val events = result.events
        test("The order is in CREATED status") { order.status shouldBe OrderStatus.CREATED }
        test("The order has two order items") { order.orderItems shouldHaveSize 2 }
        test("Two order item created event are returned") { events shouldHaveSize 2 }
        context("The first order item") {
          val item = order.orderItems[0]
          test("is in CREATED status") { item.status shouldBe OrderItemStatus.CREATED }
          test("is for STRAWBERRY") { item.product shouldBe Product.STRAWBERRY }
          test("has quantity of 4") { item.quantity shouldBe 4 }
          test("has an event for its creation") { events.map { it.itemId } shouldContain item.id }
        }
        context("The second order item") {
          val item = order.orderItems[1]
          test("is in CREATED status") { item.status shouldBe OrderItemStatus.CREATED }
          test("is for STRAWBERRY") { item.product shouldBe Product.STRAWBERRY }
          test("has quantity of 5") { item.quantity shouldBe 5 }
          test("has an event for its creation") { events.map { it.itemId } shouldContain item.id }
        }
      }
    })
