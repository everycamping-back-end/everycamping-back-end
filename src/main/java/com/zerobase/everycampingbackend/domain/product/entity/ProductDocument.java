package com.zerobase.everycampingbackend.domain.product.entity;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

import com.zerobase.everycampingbackend.domain.product.type.ProductCategory;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "product")
@Mapping(mappingPath = "elastic/product-mapping.json")
@Setting(settingPath = "elastic/product-setting.json")
public class ProductDocument {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private List<String> tags;
    private int price;
    private double avgScore;
    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime createdAt;

    public static ProductDocument from(Product product){
        return ProductDocument.builder()
            .id(product.getId())
            .name(product.getName())
            .category(product.getCategory())
            .tags(product.getTags())
            .price(product.getPrice())
            .avgScore(product.getAvgScore())
            .createdAt(product.getCreatedAt())
            .build();
    }
}
