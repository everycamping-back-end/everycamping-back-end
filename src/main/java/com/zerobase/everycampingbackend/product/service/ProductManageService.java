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
import com.zerobase.everycampingbackend.user.domain.entity.Seller;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductManageService {

    private final ProductRepository productRepository;
    private final StaticImageService staticImageService;

    @Transactional
    public void addProduct(Seller seller, ProductManageForm form, MultipartFile image, MultipartFile detailImage) throws IOException {
        log.info("상품명 (" + form.getName() + ") 추가 시도");

        S3Path imagePath = staticImageService.saveImage(image);
        S3Path detailImagePath = staticImageService.saveImage(detailImage);

        productRepository.save(Product.of(form, seller, imagePath, detailImagePath));

        log.info("상품명 (" + form.getName() + ") 추가 완료");
    }

    @Transactional
    public void updateProduct(Seller seller, long productId, ProductManageForm form, MultipartFile image, MultipartFile detailImage) throws IOException {
        Product product = getProductById(productId);

        validateProductSeller(seller, product);

        log.info("상품명 (" + form.getName() + ") 수정 시도");

        S3Path imagePath = staticImageService.editImage(product.getImagePath(), image);
        S3Path detailImagePath = staticImageService.editImage(product.getImagePath(), detailImage);

        product.setOf(form, imagePath, detailImagePath);

        productRepository.save(product);

        log.info("상품명 (" + form.getName() + ") 수정 완료");
    }

    private Product getProductById(long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional
    public void deleteProduct(Seller seller, long productId) {
        Product product = getProductById(productId);

        validateProductSeller(seller, product);

        log.info("상품명 (" + product.getName() + ") 삭제 시도");

        productRepository.delete(product);

        staticImageService.deleteImage(product.getImagePath());
        staticImageService.deleteImage(product.getDetailImagePath());

        log.info("상품명 (" + product.getName() + ") 삭제 완료");
    }

    private static void validateProductSeller(Seller seller, Product product) {
        if(!Objects.equals(product.getSeller().getId(), seller.getId())){
            throw new CustomException(ErrorCode.PRODUCT_SELLER_NOT_MATCHED);
        }
    }

    public ProductDetailDto getProductDetail(Seller seller, Long productId) {
        Product product = getProductById(productId);

        validateProductSeller(seller, product);

        log.info("상품명 (" + product.getName() + ") 판매자용 조회");

        return ProductDetailDto.from(product);
    }

    public Page<ProductDto> getProductPage(Seller seller, Pageable pageable) {
        return productRepository.findAllBySeller(seller, pageable)
            .map(ProductDto::from);
    }
}
