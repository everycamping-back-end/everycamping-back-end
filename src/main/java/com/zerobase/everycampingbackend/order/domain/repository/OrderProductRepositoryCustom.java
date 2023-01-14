package com.zerobase.everycampingbackend.order.domain.repository;

import com.zerobase.everycampingbackend.order.domain.dto.OrderProductByCustomerDto;
import com.zerobase.everycampingbackend.order.domain.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.order.domain.form.SearchOrderByCustomerForm;
import com.zerobase.everycampingbackend.order.domain.form.SearchOrderBySellerForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderProductRepositoryCustom {

    Page<OrderProductByCustomerDto> searchByCustomer(SearchOrderByCustomerForm form,
        Long customerId, Pageable pageable);

    Page<OrderProductBySellerDto> searchBySeller(SearchOrderBySellerForm form,
        Long sellerId, Pageable pageable);
}
