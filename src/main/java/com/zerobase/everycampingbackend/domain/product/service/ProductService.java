package com.zerobase.everycampingbackend.domain.product.service;

import com.zerobase.everycampingbackend.domain.product.dto.ProductDetailDto;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDto;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.form.ProductSearchForm;
import com.zerobase.everycampingbackend.domain.product.repository.ProductRepository;
import com.zerobase.everycampingbackend.domain.product.repository.ProductSearchQueryRepository;
import com.zerobase.everycampingbackend.domain.product.type.ProductCategory;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSearchQueryRepository searchQueryRepository;

    public ProductDetailDto getProductDetail(Long productId) {
        return ProductDetailDto.from(productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    public Slice<ProductDto> getProducts(ProductSearchForm form, Pageable pageable){
        long start = System.currentTimeMillis();
//        Slice<ProductDto> result = productRepository.searchAll(form, pageable);
        List<Long> ids = searchQueryRepository.findByCondition(form, pageable);
        List<Product> products = productRepository.findAllByIdIn(ids);
        long end = System.currentTimeMillis();
        log.info("검색 수행 : " + (end - start) + "ms 소요");
        return new SliceImpl<>(products.stream().map(ProductDto::from).collect(Collectors.toList()));
    }

    @Transactional
    public void addReview(Product product, Integer score){
        product.setReviewCount(product.getReviewCount() + 1);
        product.setTotalScore(product.getTotalScore() + score);
        product.setAvgScore((double) product.getTotalScore() / product.getReviewCount());

        if(product.getTotalScore() < 0){
            log.error("상품 내 리뷰 총점이 음수가 됨. 확인 요망.");
        }

        productRepository.save(product);
    }

    @Transactional
    public void updateReview(Product product, Integer scoreDiff){
        product.setTotalScore(product.getTotalScore() + scoreDiff);
        product.setAvgScore((double) product.getTotalScore() / product.getReviewCount());

        if(product.getTotalScore() < 0){
            log.error("상품 내 리뷰 총점이 음수가 됨. 확인 요망.");
        }

        productRepository.save(product);
    }

    @Transactional
    public void deleteReview(Product product, Integer score){
        product.setReviewCount(product.getReviewCount() - 1);
        product.setTotalScore(product.getTotalScore() - score);
        product.setAvgScore((double) product.getTotalScore() / product.getReviewCount());

        if(product.getTotalScore() < 0){
            log.error("상품 내 리뷰 총점이 음수가 됨. 확인 요망.");
        }

        productRepository.save(product);
    }
    public List<String> getCategories() {
        return Stream.of(ProductCategory.values()).map(Enum::name)
            .collect(Collectors.toList());
    }

    public Product getProductById(Long productId){
        return productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
