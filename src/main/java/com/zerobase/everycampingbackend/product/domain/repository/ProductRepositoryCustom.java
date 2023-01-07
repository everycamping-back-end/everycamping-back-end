package com.zerobase.everycampingbackend.product.domain.repository;

import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.form.ProductSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductDto> searchAll(ProductSearchForm form, Pageable pageable);
}
