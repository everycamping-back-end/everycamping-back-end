package com.zerobase.everycampingbackend.product.controller;

import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.form.ProductSearchForm;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> searchProduct(@ModelAttribute ProductSearchForm form, Pageable pageable){
        return ResponseEntity.ok(productService.getProducts(form, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailDto> getProductDetail(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }
}
