package com.zerobase.everycampingbackend.product.service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.repository.ProductRepository;
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
public class ProductManageService {

    private final ProductRepository productRepository;

    @Transactional
    public void addProduct(ProductManageForm form) {
        // 토큰 통해 받아오는 객체에서 판매자 추출

        log.info("상품명 (" + form.getName() + ") 추가 시도");

        productRepository.save(Product.from(form));

        log.info("상품명 (" + form.getName() + ") 추가 완료");
    }

    @Transactional
    public void updateProduct(long productId, ProductManageForm form) {
        Product product = getProductById(productId);

        // 토큰 통해 받아오는 유저객체와 product 통해 받아오는 유저객체 id 일치 여부 확인

        log.info("상품명 (" + form.getName() + ") 수정 시도");

        product.setFrom(form);

        productRepository.save(product);

        log.info("상품명 (" + form.getName() + ") 수정 완료");
    }

    private Product getProductById(long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public ProductDetailDto getProductDetail(Long productId) {
        Product product = getProductById(productId);

        // 토큰 통해 받아오는 유저객체와 product 통해 받아오는 유저객체 id 일치 여부 확인

        log.info("상품명 (" + product.getName() + ") 판매자용 조회");

        return ProductDetailDto.from(product);
    }

    public Page<ProductDto> getProductPage(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(ProductDto::from);
    }
}
