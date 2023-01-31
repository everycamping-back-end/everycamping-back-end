package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.product.service.ProductManageService;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDto;
import com.zerobase.everycampingbackend.domain.product.form.ProductManageForm;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manage/products")
public class ProductManageController {

    private final ProductManageService productManageService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addProduct(@AuthenticationPrincipal Seller seller,
        @RequestPart @Valid ProductManageForm form, @RequestPart(required = false) MultipartFile image,
        @RequestPart(required = false) MultipartFile detailImage) throws IOException, TaskRejectedException {
        productManageService.addProduct(seller, form, image, detailImage);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{productId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateProduct(@AuthenticationPrincipal Seller seller,
        @PathVariable Long productId,
        @RequestPart @Valid ProductManageForm form, @RequestPart(required = false) MultipartFile image,
        @RequestPart(required = false) MultipartFile detailImage) throws IOException, TaskRejectedException {
        productManageService.updateProduct(seller, productId, form, image, detailImage);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal Seller seller,
        @PathVariable Long productId) throws TaskRejectedException{
        productManageService.deleteProduct(seller, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailDto> getProductDetail(
        @AuthenticationPrincipal Seller seller,
        @PathVariable Long productId) {
        return ResponseEntity.ok(productManageService.getProductDetail(seller, productId));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProductPage(
        @AuthenticationPrincipal Seller seller, Pageable pageable) {
        return ResponseEntity.ok(productManageService.getProductPage(seller, pageable));
    }
}
