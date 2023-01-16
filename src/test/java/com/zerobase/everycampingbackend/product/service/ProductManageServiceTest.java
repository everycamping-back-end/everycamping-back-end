package com.zerobase.everycampingbackend.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.zerobase.everycampingbackend.common.staticimage.dto.S3Path;
import com.zerobase.everycampingbackend.common.staticimage.service.StaticImageService;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.domain.repository.ProductRepository;
import com.zerobase.everycampingbackend.product.type.ProductCategory;
import com.zerobase.everycampingbackend.user.domain.entity.Seller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(SpringExtension.class)
class ProductManageServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StaticImageService staticImageService;

    @InjectMocks
    private ProductManageService productManageService;

    private final ProductManageForm form = ProductManageForm.builder()
        .category(ProductCategory.TENT)
        .name("텐트입니다")
        .price(100000)
        .onSale(true)
        .description("아주 좋은 텐트입니다")
        .stock(10)
        .tags(List.of("따뜻함", "안락", "고퀄"))
        .build();

    Seller seller = Seller.builder().nickName("판매자1").build();
    private final Product product = Product.builder()
        .name("상품1")
        .seller(seller)
        .build();
    private final Product product2 = Product.builder()
        .name("상품1")
        .seller(Seller.builder().nickName("판매자2").build())
        .build();
    private final Page<Product> products = new PageImpl<>(List.of(product, product2));
    @Test
    @DisplayName("상품 추가 성공")
    void success_addProduct() throws IOException {
        // given
        given(staticImageService.saveImage(any()))
            .willReturn(new S3Path("uri", "path"));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        // when
        productManageService.addProduct(seller, form);

        // then
        verify(productRepository).save(captor.capture());

        assertEquals(form.getName(), captor.getValue().getName());
        assertEquals(form.getPrice(), captor.getValue().getPrice());
        assertEquals(form.getOnSale(), captor.getValue().isOnSale());
        assertEquals(form.getDescription(), captor.getValue().getDescription());
        assertEquals(form.getStock(), captor.getValue().getStock());
        assertEquals("uri", captor.getValue().getImageUri());
        assertEquals("path", captor.getValue().getImagePath());
        assertEquals("uri", captor.getValue().getDetailImageUri());
        assertEquals("path", captor.getValue().getDetailImagePath());
        for(String tag : form.getTags()){
            assertTrue(captor.getValue().getTags().contains(tag));
        }
    }

    @Test
    @DisplayName("상품 수정 성공")
    void success_updateProduct() throws IOException {
        // given
        form.setName("텐트2");
        product.setImageUri("asfdasfasf");
        product.setImagePath("ewfwfwf");
        product.setDetailImageUri("trbrtbrt");
        product.setDetailImagePath("jojoinono");
        given(productRepository.findById(anyLong()))
            .willReturn(Optional.of(product));
        given(staticImageService.editImage(anyString(), any()))
            .willReturn(new S3Path("uri", "path"));
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        // when
        productManageService.updateProduct(seller,1L, form);

        // then
        verify(productRepository).save(captor.capture());
        assertEquals(form.getName(), captor.getValue().getName());
        assertEquals(form.getPrice(), captor.getValue().getPrice());
        assertEquals(form.getOnSale(), captor.getValue().isOnSale());
        assertEquals(form.getDescription(), captor.getValue().getDescription());
        assertEquals(form.getStock(), captor.getValue().getStock());
        assertEquals("uri", captor.getValue().getImageUri());
        assertEquals("path", captor.getValue().getImagePath());
        assertEquals("uri", captor.getValue().getDetailImageUri());
        assertEquals("path", captor.getValue().getDetailImagePath());
        for(String tag : form.getTags()){
            assertTrue(captor.getValue().getTags().contains(tag));
        }
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void success_deleteProduct(){
        // given
        given(productRepository.findById(anyLong()))
            .willReturn(Optional.of(product));
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        // when
        productManageService.deleteProduct(seller, 1L);

        // then
        verify(productRepository).delete(captor.capture());
    }

    @Test
    @DisplayName("상품 목록 조회 성공")
    void success_getProducts(){
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);
        given(productRepository.findAllBySeller(seller, pageRequest))
            .willReturn(products);

        // when
        Page<ProductDto> result = productManageService.getProductPage(seller, pageRequest);

        // then
        assertEquals(products.getSize(), result.getSize());
        assertEquals(product.getName(), result.getContent().get(0).getName());
        assertEquals(product2.getName(), result.getContent().get(1).getName());
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void success_getProductDetail(){
        // given
        given(productRepository.findById(anyLong()))
            .willReturn(Optional.of(product));

        // when
        ProductDetailDto result = productManageService.getProductDetail(seller, 1L);

        // then
        assertEquals(product.getName(), result.getName());
    }
}