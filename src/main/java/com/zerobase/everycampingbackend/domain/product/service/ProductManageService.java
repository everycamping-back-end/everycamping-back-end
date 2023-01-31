package com.zerobase.everycampingbackend.domain.product.service;

import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import com.zerobase.everycampingbackend.domain.staticimage.dto.S3Path;
import com.zerobase.everycampingbackend.domain.staticimage.service.StaticImageService;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDto;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.form.ProductManageForm;
import com.zerobase.everycampingbackend.domain.product.repository.ProductRepository;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductManageService {

    private final ProductRepository productRepository;
    private final StaticImageService staticImageService;

    @Transactional
    public void addProduct(Seller seller, ProductManageForm form, MultipartFile image,
        MultipartFile detailImage) throws IOException, TaskRejectedException {
        log.info("상품명 (" + form.getName() + ") 추가 시도");

        validateSeller(seller);

        S3Path imagePath = staticImageService.saveImage(image);
        S3Path detailImagePath = staticImageService.saveImage(detailImage);

        productRepository.save(Product.of(form, seller, imagePath, detailImagePath));

        log.info("상품명 (" + form.getName() + ") 추가 완료");
    }

    @Transactional
    public void updateProduct(Seller seller, long productId, ProductManageForm form,
        MultipartFile image, MultipartFile detailImage) throws IOException, TaskRejectedException {
        Product product = getProductById(productId);

        validateProductSeller(seller, product);

        log.info("상품명 (" + form.getName() + ") 수정 시도");

        S3Path imagePath = staticImageService.editImage(product.getImagePath(), image);
        if (ObjectUtils.isEmpty(image)) {
            imagePath.setImageUri(product.getImageUri());
            imagePath.setImagePath(product.getImagePath());
        }

        S3Path detailImagePath = staticImageService.editImage(product.getImagePath(), detailImage);
        if (ObjectUtils.isEmpty(detailImage)) {
            detailImagePath.setImageUri(product.getDetailImageUri());
            detailImagePath.setImagePath(product.getDetailImagePath());
        }

        product.setOf(form, imagePath, detailImagePath);

        productRepository.save(product);

        log.info("상품명 (" + form.getName() + ") 수정 완료");
    }

    private Product getProductById(long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional
    public void deleteProduct(Seller seller, long productId) throws TaskRejectedException {
        Product product = getProductById(productId);

        validateProductSeller(seller, product);

        log.info("상품명 (" + product.getName() + ") 삭제 시도");

        staticImageService.deleteImage(product.getImagePath());
        staticImageService.deleteImage(product.getDetailImagePath());

        productRepository.delete(product);

        log.info("상품명 (" + product.getName() + ") 삭제 완료");
    }

    private static void validateProductSeller(Seller seller, Product product) {
        if (!Objects.equals(product.getSeller().getId(), seller.getId())) {
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

    public void validateSeller(Seller seller) {
        if (!seller.isConfirmed()) {
            throw new CustomException(ErrorCode.SELLER_NOT_CONFIRMED);
        }
    }
}
