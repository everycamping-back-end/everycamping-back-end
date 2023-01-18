package com.zerobase.everycampingbackend.domain.order.repository;

import com.zerobase.everycampingbackend.domain.order.dto.OrderProductByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.domain.order.form.SearchOrderByCustomerForm;
import com.zerobase.everycampingbackend.domain.order.form.SearchOrderBySellerForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderProductRepositoryCustom {

    Page<OrderProductByCustomerDto> searchByCustomer(SearchOrderByCustomerForm form,
        Long customerId, Pageable pageable);

    Page<OrderProductBySellerDto> searchBySeller(SearchOrderBySellerForm form,
        Long sellerId, Pageable pageable);
}
