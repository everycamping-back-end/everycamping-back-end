package com.zerobase.everycampingbackend.product.service;

import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        productRepository.save(Product.builder()
            .name(form.getName())
            .category(form.getCategory())
            .price(form.getPrice())
            .onSale(form.getOnSale())
            .stock(form.getStock())
            .description(form.getDescription())
            .imagePath(form.getImagePath())
            .detailImagePath(form.getDetailImagePath())
            .tags(form.getTags())
            .build());

        log.info("상품명 (" + form.getName() + ") 추가 완료");
    }
}
