package com.zerobase.everycampingbackend.domain.order.service;

import com.zerobase.everycampingbackend.domain.order.dto.OrderByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderDetailByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductDetailBySellerDto;
import com.zerobase.everycampingbackend.domain.order.entity.OrderProduct;
import com.zerobase.everycampingbackend.domain.order.entity.Orders;
import com.zerobase.everycampingbackend.domain.order.form.GetOrderProductBySellerForm;
import com.zerobase.everycampingbackend.domain.order.form.GetOrdersByCustomerForm;
import com.zerobase.everycampingbackend.domain.order.form.OrderForm;
import com.zerobase.everycampingbackend.domain.order.form.OrderForm.OrderProductForm;
import com.zerobase.everycampingbackend.domain.order.repository.OrderProductRepository;
import com.zerobase.everycampingbackend.domain.order.repository.OrdersRepository;
import com.zerobase.everycampingbackend.domain.order.type.OrderStatus;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.service.ProductService;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;
    private final OrdersRepository ordersRepository;
    private final OrderProductRepository orderProductRepository;


    @Transactional
    public void order(Customer customer, OrderForm form) {

        Orders orders = ordersRepository.save(Orders.builder()
            .customer(customer)
            .name(form.getName())
            .address(form.getAddress())
            .phone(form.getPhone())
            .request(form.getRequest())
            .orderProductCount(form.getOrderProductFormList().size())
            .totalAmount(0)
            .build());

        form.getOrderProductFormList().forEach(f -> orderProduct(orders, f));
    }

    private void orderProduct(Orders orders, OrderProductForm orderProductForm) {

        Product product = productService.getProductById(orderProductForm.getProductId());
        if (!product.isOnSale()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_ON_SALE);
        }
        if (product.getStock() < orderProductForm.getQuantity()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
        }
        if (orders.getRepresentProductName() == null) {
            orders.setRepresentProductName(product.getName());
        }

        orders.setTotalAmount(
            orders.getTotalAmount() + orderProductForm.getQuantity() * product.getPrice());
        product.setStock(product.getStock() - orderProductForm.getQuantity());

        orderProductRepository.save(
            OrderProduct.of(orders, product, orderProductForm.getQuantity()));
    }

    public Page<OrderByCustomerDto> getOrdersByCustomer(GetOrdersByCustomerForm form,
        Long customerId, Pageable pageable) {

        if (form.getEndDate() == null && form.getStartDate() == null) {
            Page<Orders> ordersPage = ordersRepository.findAllByCustomerId(customerId,
                pageable);

            return ordersPage.map(OrderByCustomerDto::from);
        }

        LocalDateTime start = form.getStartDate() == null ? null
            : form.getStartDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime end = form.getEndDate() == null ? null
            : form.getEndDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime().with(LocalTime.MAX);

        Page<Orders> ordersPage = ordersRepository.findAllByCustomerIdAndCreatedAtBetween(
            customerId,
            pageable, start, end);

        return ordersPage.map(OrderByCustomerDto::from);
    }

    public OrderDetailByCustomerDto getOrdersDetailByCustomer(Long orderId, Long customerId) {

        List<OrderDetailByCustomerDto> list = ordersRepository.getOrderDetailByCustomer(
            orderId);
        if(list.isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUNT);
        }

        OrderDetailByCustomerDto dto = list.get(0);
        if(!dto.getCustomerId().equals(customerId)) {
            throw new CustomException(ErrorCode.ORDER_SELECT_NOT_AUTHORISED);
        }

        return dto;
    }

    public Page<OrderProductBySellerDto> getOrderProductBySeller(GetOrderProductBySellerForm form,
        Long sellerId, Pageable pageable) {
        return orderProductRepository.getOrderProductsBySeller(form, sellerId, pageable);
    }

    public OrderProductDetailBySellerDto getOrderProductDetailBySeller(Long orderProductId,
        Long sellerId) {

        List<OrderProductDetailBySellerDto> list = orderProductRepository.getOrderProductDetailBySeller(
            orderProductId);

        if(list.isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_PRODUCT_NOT_FOUNT);
        }

        OrderProductDetailBySellerDto dto = list.get(0);

        if(!sellerId.equals(dto.getSellerId())) {
            throw new CustomException(ErrorCode.ORDER_PRODUCT_SELECT_NOT_AUTHORISED);
        }
        return dto;
    }


    @Transactional
    public void confirm(Customer customer, Long orderProductId) {

        OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUNT));

        if (!orderProduct.getOrders().getCustomer().getId().equals(customer.getId())) {
            throw new CustomException(ErrorCode.ORDER_CHANGE_STATUS_NOT_AUTHORISED);
        }

        if (!orderProduct.getStatus().equals(OrderStatus.COMPLETE)) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CONFIRMED_OR_CANCELED);
        }

        orderProduct.setStatus(OrderStatus.CONFIRM);
        orderProduct.setConfirmedAt(LocalDateTime.now());
    }

    @Transactional
    public void cancel(Customer customer, Long orderProductId) {

        OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUNT));

        if (!orderProduct.getOrders().getCustomer().getId().equals(customer.getId())) {
            throw new CustomException(ErrorCode.ORDER_CHANGE_STATUS_NOT_AUTHORISED);
        }

        if (!orderProduct.getStatus().equals(OrderStatus.COMPLETE)) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CONFIRMED_OR_CANCELED);
        }

        orderProduct.getProduct().setStock(
            orderProduct.getProduct().getStock() - orderProduct.getQuantity());

        orderProduct.setStatus(OrderStatus.CANCEL);
    }
}
