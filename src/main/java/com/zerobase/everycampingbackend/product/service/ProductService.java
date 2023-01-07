package com.zerobase.everycampingbackend.product.service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.form.ProductSearchForm;
import com.zerobase.everycampingbackend.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductDetailDto getProductDetail(Long productId) {
        return ProductDetailDto.from(productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    public Page<ProductDto> getProducts(ProductSearchForm form, Pageable pageable){
        long start = System.currentTimeMillis();
        Page<ProductDto> result = productRepository.searchAll(form, pageable);
        long end = System.currentTimeMillis();
        log.info("검색 수행 : " + (end - start) + "ms 소요");
        return result;
    }
}
