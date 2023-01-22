package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.order.dto.OrderByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderDetailByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.domain.order.form.GetOrderProductBySellerForm;
import com.zerobase.everycampingbackend.domain.order.form.GetOrdersByCustomerForm;
import com.zerobase.everycampingbackend.domain.order.form.OrderForm;
import com.zerobase.everycampingbackend.domain.order.service.OrderService;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> order(@AuthenticationPrincipal Customer customer,
        @RequestBody @Valid OrderForm form) {
        orderService.order(customer, form);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer")
    public ResponseEntity<Page<OrderByCustomerDto>> getOrdersByCustomer(
        @AuthenticationPrincipal Customer customer,
        @ModelAttribute GetOrdersByCustomerForm form,
        @PageableDefault(sort="createdAt", direction = Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(orderService.getOrdersByCustomer(form, customer.getId(), pageable));
    }

    @GetMapping("/customer/{orderId}")
    public ResponseEntity<OrderDetailByCustomerDto> getOrdersDetailByCustomer(
        @AuthenticationPrincipal Customer customer,
        @PathVariable @NotNull Long orderId) {

        return ResponseEntity.ok(orderService.getOrdersDetailByCustomer(orderId, customer.getId()));
    }

    @GetMapping("/seller")
    public ResponseEntity<Page<OrderProductBySellerDto>> getOrdersBySeller(
        @AuthenticationPrincipal Seller seller,
        @ModelAttribute GetOrderProductBySellerForm form,
        @PageableDefault(sort="createdAt", direction = Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(orderService.getOrderProductBySeller(form, seller.getId(), pageable));
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
