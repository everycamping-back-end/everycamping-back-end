package com.zerobase.everycampingbackend.domain.product.dto;

import com.zerobase.everycampingbackend.domain.product.type.ProductCategory;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String sellerName;
    private ProductCategory category;
    private String name;
    private int price;
    private String imageUri;
    private int reviewCount;
    private double avgScore;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
            .id(product.getId())
            .sellerName(product.getSeller().getNickName())
            .category(product.getCategory())
            .name(product.getName())
            .price(product.getPrice())
            .imageUri(product.getImageUri())
            .reviewCount(product.getReviewCount())
            .avgScore(product.getReviewCount() == 0 ? 0
                : product.getTotalScore() / (double) product.getReviewCount())
            .createdAt(product.getCreatedAt())
            .modifiedAt(product.getModifiedAt())
            .build();
    }
}
