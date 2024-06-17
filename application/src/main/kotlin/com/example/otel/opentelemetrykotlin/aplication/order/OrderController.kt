package com.example.otel.opentelemetrykotlin.aplication.order

import com.example.otel.opentelemetrykotlin.domain.order.OrderId
import com.example.otel.opentelemetrykotlin.domain.order.OrderService
import java.net.URI
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val ORDERS_MAPPING = "/orders"

@RestController
@RequestMapping(ORDERS_MAPPING)
class OrderController(private val orderService: OrderService) {
  @PostMapping("/")
  fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<OrderResponse> {
    val order = orderService.createOrder(request.products.map { it.toProductPair() })
    return ResponseEntity.created(URI.create("$ORDERS_MAPPING/${order.id.id}"))
        .body(order.toOrderResponse())
  }

  @GetMapping("/{id}")
  fun getOrder(@PathVariable id: UUID): ResponseEntity<OrderResponse> =
      orderService.getOrder(OrderId(id))?.let { ResponseEntity.ok(it.toOrderResponse()) }
          ?: ResponseEntity.notFound().build()
}
