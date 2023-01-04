package com.zerobase.everycampingbackend.product.controller;

import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.service.ProductManageService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
