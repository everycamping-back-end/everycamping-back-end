package com.zerobase.everycampingbackend.product.domain.dto;

import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.type.ProductCategory;
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
    private String imagePath;
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
            .imagePath(product.getImagePath())
            .reviewCount(product.getReviewCount())
            .avgScore(product.getReviewCount() == 0 ? 0
                : product.getTotalScore() / (double) product.getReviewCount())
            .createdAt(product.getCreatedAt())
            .modifiedAt(product.getModifiedAt())
            .build();
    }
}
