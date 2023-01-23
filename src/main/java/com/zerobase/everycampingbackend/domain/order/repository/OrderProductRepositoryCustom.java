package com.zerobase.everycampingbackend.domain.order.repository;

import com.zerobase.everycampingbackend.domain.order.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductDetailBySellerDto;
import com.zerobase.everycampingbackend.domain.order.form.GetOrderProductBySellerForm;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderProductRepositoryCustom {

    Page<OrderProductBySellerDto> getOrderProductsBySeller(GetOrderProductBySellerForm form,
        Long sellerId, Pageable pageable);

    List<OrderProductDetailBySellerDto> getOrderProductDetailBySeller(Long orderProductId);
}
