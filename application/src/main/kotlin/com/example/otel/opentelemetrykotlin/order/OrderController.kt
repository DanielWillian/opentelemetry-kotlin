package com.example.otel.opentelemetrykotlin.order

import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(private val orderService: OrderService) {
    @PostMapping("/")
    fun createOrder(@RequestBody request: CreateOrderRequest): OrderResponse {
        val order = orderService.createOrder(request.products.map { it.toProductPair() })
        return order.toOrderResponse()
    }

    @GetMapping("/{id}")
    fun getOrder(@PathVariable id: UUID): ResponseEntity<OrderResponse> =
        orderService.getOrder(OrderId(id))?.let { ResponseEntity.ok(it.toOrderResponse()) }
            ?: ResponseEntity.notFound().build()
}
