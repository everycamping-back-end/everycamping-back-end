package com.zerobase.everycampingbackend.order.controller;

import com.zerobase.everycampingbackend.order.domain.dto.OrderProductByCustomerDto;
import com.zerobase.everycampingbackend.order.domain.form.OrderForm;
import com.zerobase.everycampingbackend.order.domain.form.SearchOrderByCustomerForm;
import com.zerobase.everycampingbackend.order.service.OrderService;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity order(@RequestBody OrderForm form) {
        orderService.order(form);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<OrderProductByCustomerDto>> getOrdersByCustomer(
        @ModelAttribute SearchOrderByCustomerForm form,
        @RequestParam @NotNull Long customerId,
        Pageable pageable) {

        return ResponseEntity.ok(orderService.getOrdersByCustomer(form, customerId, pageable));
    }

//    @GetMapping
//    public ResponseEntity getOrdersBySeller(@RequestParam @NotNull Long sellerId) {
//
//    }
}
