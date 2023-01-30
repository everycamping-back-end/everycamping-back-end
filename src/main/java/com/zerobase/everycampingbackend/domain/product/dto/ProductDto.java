package com.zerobase.everycampingbackend.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.type.ProductCategory;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
            .id(product.getId())
            .sellerName(product.getSeller() == null ? "" : product.getSeller().getNickName())
            .category(product.getCategory())
            .name(product.getName())
            .price(product.getPrice())
            .imageUri(product.getImageUri())
            .reviewCount(product.getReviewCount())
            .avgScore(product.getAvgScore())
            .createdAt(product.getCreatedAt())
            .modifiedAt(product.getModifiedAt())
            .build();
    }
}
