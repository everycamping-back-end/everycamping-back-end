package com.zerobase.everycampingbackend.domain.order.service;

import com.zerobase.everycampingbackend.domain.order.dto.OrderByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.domain.order.entity.OrderProduct;
import com.zerobase.everycampingbackend.domain.order.entity.Orders;
import com.zerobase.everycampingbackend.domain.order.form.OrderForm;
import com.zerobase.everycampingbackend.domain.order.form.OrderForm.OrderProductForm;
import com.zerobase.everycampingbackend.domain.order.form.SearchOrderByCustomerForm;
import com.zerobase.everycampingbackend.domain.order.form.SearchOrderBySellerForm;
import com.zerobase.everycampingbackend.domain.order.repository.OrderProductRepository;
import com.zerobase.everycampingbackend.domain.order.repository.OrdersRepository;
import com.zerobase.everycampingbackend.domain.order.type.OrderStatus;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.service.ProductService;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<OrderByCustomerDto> getOrdersByCustomer(Long customerId, Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAllByCustomerId(customerId,
            pageable);

        return ordersPage.map(OrderByCustomerDto::from);
    }

    public Page<OrderProductByCustomerDto> getOrdersDetailByCustomer(SearchOrderByCustomerForm form,
        Long customerId, Pageable pageable) {
        return orderProductRepository.searchByCustomer(form, customerId, pageable);
    }

    public Page<OrderProductBySellerDto> getOrdersBySeller(SearchOrderBySellerForm form,
        Long sellerId, Pageable pageable) {
        return orderProductRepository.searchBySeller(form, sellerId, pageable);
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
