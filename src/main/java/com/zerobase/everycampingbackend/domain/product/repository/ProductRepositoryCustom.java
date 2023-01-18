package com.zerobase.everycampingbackend.domain.product.repository;

import com.zerobase.everycampingbackend.domain.product.dto.ProductDto;
import com.zerobase.everycampingbackend.domain.product.form.ProductSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductDto> searchAll(ProductSearchForm form, Pageable pageable);
}
