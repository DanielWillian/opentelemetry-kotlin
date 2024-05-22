package com.example.otel.opentelemetrykotlin

import com.example.otel.opentelemetrykotlin.order.OrderFactory
import com.example.otel.opentelemetrykotlin.order.OrderFactoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DomainBeans {
    @Bean fun orderFactory(): OrderFactory = OrderFactoryImpl()
}
