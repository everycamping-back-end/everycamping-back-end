package com.zerobase.everycampingbackend.domain.order.repository;

import com.zerobase.everycampingbackend.domain.order.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.domain.order.form.GetOrderProductBySellerForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderProductRepositoryCustom {

    Page<OrderProductBySellerDto> getOrderProductBySeller(GetOrderProductBySellerForm form,
        Long sellerId, Pageable pageable);
}
