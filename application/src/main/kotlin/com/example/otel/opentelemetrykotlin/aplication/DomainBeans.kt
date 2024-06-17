package com.example.otel.opentelemetrykotlin.aplication

import com.example.otel.opentelemetrykotlin.domain.order.OrderFactory
import com.example.otel.opentelemetrykotlin.domain.order.OrderFactoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DomainBeans {
  @Bean fun orderFactory(): OrderFactory = OrderFactoryImpl()
}
