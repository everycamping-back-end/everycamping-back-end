package com.zerobase.everycampingbackend.domain.product.repository;

import com.zerobase.everycampingbackend.domain.product.dto.ProductDto;
import com.zerobase.everycampingbackend.domain.product.form.ProductSearchForm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
    Slice<ProductDto> searchAll(ProductSearchForm form, Pageable pageable);
}
