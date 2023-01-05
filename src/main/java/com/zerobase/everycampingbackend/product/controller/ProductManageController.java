package com.zerobase.everycampingbackend.product.controller;

import com.zerobase.everycampingbackend.product.domain.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.service.ProductManageService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/products/manage")
public class ProductManageController {

    private final ProductManageService productManageService;

    @PostMapping
    public ResponseEntity<Boolean> addProduct(@RequestBody @Valid ProductManageForm form) {
        productManageService.addProduct(form);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Boolean> updateProduct(@PathVariable Long productId,
        @RequestBody @Valid ProductManageForm form) {
        productManageService.updateProduct(productId, form);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long productId) {
        productManageService.deleteProduct(productId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailDto> getProductDetail(@PathVariable Long productId){
        return ResponseEntity.ok(productManageService.getProductDetail(productId));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProductPage(Pageable pageable){
        return ResponseEntity.ok(productManageService.getProductPage(pageable));
    }
}
