package com.zerobase.everycampingbackend.product.controller;

import com.zerobase.everycampingbackend.product.domain.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.service.ProductManageService;
import com.zerobase.everycampingbackend.user.domain.entity.Seller;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manage/products")
public class ProductManageController {

    private final ProductManageService productManageService;

    @PostMapping
    public ResponseEntity<?> addProduct(@AuthenticationPrincipal Seller seller,
        @RequestBody @Valid ProductManageForm form) throws IOException {
        productManageService.addProduct(seller, form);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@AuthenticationPrincipal Seller seller,
        @PathVariable Long productId,
        @RequestBody @Valid ProductManageForm form) throws IOException {
        productManageService.updateProduct(seller, productId, form);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal Seller seller,
        @PathVariable Long productId) {
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
