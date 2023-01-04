package com.zerobase.everycampingbackend.product.domain.entity;

import com.zerobase.everycampingbackend.common.BaseEntity;
import com.zerobase.everycampingbackend.product.type.ProductCategory;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // private Seller seller;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private String name;
    private String description;
    private int stock;
    private int price;
    private String imagePath;
    private String detailImagePath;
    private boolean onSale;
    @ElementCollection
    private List<String> tags;
    private int reviewCount;
    private int totalScore;
}
