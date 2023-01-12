package com.zerobase.everycampingbackend.order.service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.order.domain.entity.OrderProduct;
import com.zerobase.everycampingbackend.order.domain.entity.Orders;
import com.zerobase.everycampingbackend.order.domain.form.OrderForm;
import com.zerobase.everycampingbackend.order.domain.form.OrderForm.OrderProductForm;
import com.zerobase.everycampingbackend.order.domain.repository.OrderProductRepository;
import com.zerobase.everycampingbackend.order.domain.repository.OrdersRepository;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.service.ProductService;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final CustomerService customerService;
  private final ProductService productService;
  private final OrdersRepository ordersRepository;
  private final OrderProductRepository orderProductRepository;


  @Transactional
  public void order(OrderForm form) {

    //로그인한 Customer 관련 로직 추가 예정

    Customer customer = customerService.getCustomerById(form.getCustomerId());
    Orders orders = ordersRepository.save(Orders.builder()
                                                .customer(customer)
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

    product.setStock(product.getStock()-orderProductForm.getQuantity()); //더티체킹

    orderProductRepository.save(OrderProduct.of(orders, product, orderProductForm.getQuantity()));
  }
}
