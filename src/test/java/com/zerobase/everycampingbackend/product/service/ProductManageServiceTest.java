package com.zerobase.everycampingbackend.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.repository.ProductRepository;
import com.zerobase.everycampingbackend.product.type.ProductCategory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
class ProductManageServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductManageService productManageService;

    private final ProductManageForm form = ProductManageForm.builder()
        .category(ProductCategory.TENT)
        .name("텐트입니다")
        .price(100000)
        .onSale(true)
        .description("아주 좋은 텐트입니다")
        .stock(10)
        .imagePath("이미지경로")
        .detailImagePath("상세이미지경로")
        .tags(List.of("따뜻함", "안락", "고퀄"))
        .build();

    private final Product product = Product.builder().build();
    @Test
    @DisplayName("상품 추가 성공")
    void success_addProduct(){
        // given
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        // when
        productManageService.addProduct(form);

        // then
        verify(productRepository).save(captor.capture());

        assertEquals(form.getName(), captor.getValue().getName());
        assertEquals(form.getPrice(), captor.getValue().getPrice());
        assertEquals(form.getOnSale(), captor.getValue().isOnSale());
        assertEquals(form.getDescription(), captor.getValue().getDescription());
        assertEquals(form.getStock(), captor.getValue().getStock());
        assertEquals(form.getImagePath(), captor.getValue().getImagePath());
        assertEquals(form.getDetailImagePath(), captor.getValue().getDetailImagePath());
        for(String tag : form.getTags()){
            assertTrue(captor.getValue().getTags().contains(tag));
        }
    }

    @Test
    @DisplayName("상품 수정 성공")
    void success_updateProduct(){
        // given
        form.setName("텐트2");
        given(productRepository.findById(anyLong()))
            .willReturn(Optional.of(product));
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        // when
        productManageService.updateProduct(1L, form);

        // then
        verify(productRepository).save(captor.capture());
        assertEquals(form.getName(), captor.getValue().getName());
        assertEquals(form.getPrice(), captor.getValue().getPrice());
        assertEquals(form.getOnSale(), captor.getValue().isOnSale());
        assertEquals(form.getDescription(), captor.getValue().getDescription());
        assertEquals(form.getStock(), captor.getValue().getStock());
        assertEquals(form.getImagePath(), captor.getValue().getImagePath());
        assertEquals(form.getDetailImagePath(), captor.getValue().getDetailImagePath());
        for(String tag : form.getTags()){
            assertTrue(captor.getValue().getTags().contains(tag));
        }
    }
}