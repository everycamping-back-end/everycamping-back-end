package com.zerobase.everycampingbackend.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDto;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.form.ProductSearchForm;
import com.zerobase.everycampingbackend.domain.product.repository.ProductRepository;
import com.zerobase.everycampingbackend.domain.product.service.ProductService;
import com.zerobase.everycampingbackend.domain.product.type.ProductCategory;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import com.zerobase.everycampingbackend.domain.user.repository.SellerRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductService productService;

    Seller seller = Seller.builder().nickName("판매자1").build();
    Product product1 = Product.builder()
        .name("상품1")
        .seller(seller)
        .category(ProductCategory.TENT)
        .tags(List.of("따뜻", "단아"))
        .build();
    Product product2 = Product.builder()
        .name("상품2")
        .seller(seller)
        .category(ProductCategory.MAT)
        .tags(List.of("따뜻", "우리집"))
        .build();

    Product product3 = Product.builder()
        .name("상품3")
        .seller(seller)
        .category(ProductCategory.TABLE)
        .tags(List.of("쿨", "우리집"))
        .build();

    Product product4 = Product.builder()
        .name("텐트임")
        .seller(seller)
        .category(ProductCategory.TENT)
        .tags(List.of("럭셔리"))
        .build();

    ProductSearchForm form = ProductSearchForm.builder().build();

    @BeforeEach
    void setUp() {
        sellerRepository.save(seller);
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
    }

    @Test
    @DisplayName("고객 상품 상세 조회 성공")
    void success_getProductDetail(){
        // given
        // when
        ProductDetailDto result = productService.getProductDetail(product1.getId());

        // then
        assertEquals(product1.getName(), result.getName());
        assertEquals(product1.getCategory(), result.getCategory());
        for(int i = 0; i < product1.getTags().size(); i++){
            assertEquals(product1.getTags().get(i), result.getTags().get(i));
        }
    }

    @Test
    @DisplayName("고객 상품 상세 조회 실패 - 해당 상품 없음")
    void fail_getProductDetail_productNotFound(){
        // given
        // when
        CustomException exception = assertThrows(CustomException.class, () ->
            productService.getProductDetail(100L));

        // then
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("고객 상품 검색 성공 - 상품 이름")
    void success_getProducts_byProductName(){
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);
        form.setName("상품");
        // when
        Page<ProductDto> result = productService.getProducts(form, pageRequest);

        // then
        assertEquals(3, result.getSize());
        assertEquals(product3.getName(), result.getContent().get(0).getName());
        assertEquals(product2.getName(), result.getContent().get(1).getName());
        assertEquals(product1.getName(), result.getContent().get(2).getName());
    }

    @Test
    @DisplayName("고객 상품 검색 성공 - 상품 카테고리")
    void success_getProducts_byProductCategory(){
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);
        form.setCategory(ProductCategory.TENT);
        // when
        Page<ProductDto> result = productService.getProducts(form, pageRequest);

        // then
        assertEquals(2, result.getSize());
        assertEquals(product4.getName(), result.getContent().get(0).getName());
        assertEquals(product1.getName(), result.getContent().get(1).getName());
    }

    @Test
    @DisplayName("고객 상품 검색 성공 - 상품 태그")
    void success_getProducts_byProductTags(){
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        form.setTags(List.of("우리집"));
        Page<ProductDto> result = productService.getProducts(form, pageRequest);

        form.setTags(List.of("럭셔리"));
        Page<ProductDto> result2 = productService.getProducts(form, pageRequest);

        // then
        assertEquals(2, result.getSize());
        assertEquals(product3.getName(), result.getContent().get(0).getName());
        assertEquals(product2.getName(), result.getContent().get(1).getName());

        assertEquals(1, result2.getSize());
        assertEquals(product4.getName(), result2.getContent().get(0).getName());
    }

    @Test
    @DisplayName("고객 상품 검색 성공 - 상품 이름 + 카테고리")
    void success_getProducts_byProductNameAndCategory(){
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);
        form.setName("상품");
        form.setCategory(ProductCategory.TENT);
        // when
        Page<ProductDto> result = productService.getProducts(form, pageRequest);

        // then
        assertEquals(1, result.getSize());
        assertEquals(product1.getName(), result.getContent().get(0).getName());
    }
}