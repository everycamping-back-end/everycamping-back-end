package com.zerobase.everycampingbackend.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerobase.everycampingbackend.order.domain.dto.OrderDto;
import com.zerobase.everycampingbackend.order.domain.form.CreateOrderForm;
import com.zerobase.everycampingbackend.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  // @PostMapping
  // public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal UserDetails loggedInUser,
  // 	@RequestBody CreateOrderForm form) {
  // 	return ResponseEntity.ok(OrderDto.from(orderService.createOrder(loggedInUser, form)));
  // }

  @PostMapping
  public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderForm form) {
    return ResponseEntity.ok(OrderDto.from(orderService.createOrder(form)));
  }
}
