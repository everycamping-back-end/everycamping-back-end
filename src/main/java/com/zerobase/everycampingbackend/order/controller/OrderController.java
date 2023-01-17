package com.zerobase.everycampingbackend.order.controller;

import com.zerobase.everycampingbackend.order.domain.dto.OrderProductByCustomerDto;
import com.zerobase.everycampingbackend.order.domain.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.order.domain.form.OrderForm;
import com.zerobase.everycampingbackend.order.domain.form.SearchOrderByCustomerForm;
import com.zerobase.everycampingbackend.order.domain.form.SearchOrderBySellerForm;
import com.zerobase.everycampingbackend.order.service.OrderService;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.domain.entity.Seller;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> order(@AuthenticationPrincipal Customer customer,
        @RequestBody @Valid OrderForm form) {
        orderService.order(customer, form);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer")
    public ResponseEntity<Page<OrderProductByCustomerDto>> getOrdersByCustomer(
        @AuthenticationPrincipal Customer customer,
        @ModelAttribute SearchOrderByCustomerForm form,
        Pageable pageable) {

        return ResponseEntity.ok(orderService.getOrdersByCustomer(form, customer.getId(), pageable));
    }

    @GetMapping("/seller")
    public ResponseEntity<Page<OrderProductBySellerDto>> getOrdersBySeller(
        @AuthenticationPrincipal Seller seller,
        @ModelAttribute SearchOrderBySellerForm form,
        Pageable pageable) {

        return ResponseEntity.ok(orderService.getOrdersBySeller(form, seller.getId(), pageable));
    }

    @PatchMapping("/{orderProductId}/confirm")
    public ResponseEntity<?> confirm(@AuthenticationPrincipal Customer customer,
        @PathVariable @NotNull Long orderProductId) {

        orderService.confirm(customer, orderProductId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderProductId}/cancel")
    public ResponseEntity<?> cancel(@AuthenticationPrincipal Customer customer,
        @PathVariable @NotNull Long orderProductId) {

        orderService.cancel(customer, orderProductId);
        return ResponseEntity.ok().build();
    }
}
