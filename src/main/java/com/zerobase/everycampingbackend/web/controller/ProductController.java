package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.product.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDto;
import com.zerobase.everycampingbackend.domain.product.form.ProductSearchForm;
import com.zerobase.everycampingbackend.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public ResponseEntity<Slice<ProductDto>> searchProduct(@ModelAttribute ProductSearchForm form, Pageable pageable){
        return ResponseEntity.ok(productService.getProducts(form, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailDto> getProductDetail(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }
}
