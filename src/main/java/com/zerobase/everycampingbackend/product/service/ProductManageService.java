package com.zerobase.everycampingbackend.product.service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.common.staticimage.dto.S3Path;
import com.zerobase.everycampingbackend.common.staticimage.service.StaticImageService;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.domain.repository.ProductRepository;
import java.io.IOException;
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
    private final StaticImageService staticImageService;

    @Transactional
    public void addProduct(ProductManageForm form) throws IOException {
        // 토큰 통해 받아오는 객체에서 판매자 추출

        log.info("상품명 (" + form.getName() + ") 추가 시도");

        S3Path imagePath = staticImageService.saveImage(form.getImage());
        S3Path detailImagePath = staticImageService.saveImage(form.getDetailImage());

        productRepository.save(Product.of(form, imagePath, detailImagePath));

        log.info("상품명 (" + form.getName() + ") 추가 완료");
    }

    @Transactional
    public void updateProduct(long productId, ProductManageForm form) throws IOException {
        Product product = getProductById(productId);

        // 토큰 통해 받아오는 유저객체와 product 통해 받아오는 유저객체 id 일치 여부 확인

        log.info("상품명 (" + form.getName() + ") 수정 시도");

        S3Path imagePath = staticImageService.editImage(product.getImagePath(), form.getImage());
        S3Path detailImagePath = staticImageService.editImage(product.getImagePath(), form.getDetailImage());

        product.setOf(form, imagePath, detailImagePath);

        productRepository.save(product);

        log.info("상품명 (" + form.getName() + ") 수정 완료");
    }

    private Product getProductById(long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional
    public void deleteProduct(long productId) {
        Product product = getProductById(productId);

        // 토큰 통해 받아오는 유저객체와 product 통해 받아오는 유저객체 id 일치 여부 확인

        log.info("상품명 (" + product.getName() + ") 삭제 시도");

        productRepository.delete(product);

        staticImageService.deleteImage(product.getImagePath());
        staticImageService.deleteImage(product.getDetailImagePath());

        log.info("상품명 (" + product.getName() + ") 삭제 완료");
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
