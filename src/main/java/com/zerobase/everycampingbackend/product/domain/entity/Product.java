package com.zerobase.everycampingbackend.product.domain.entity;

import com.zerobase.everycampingbackend.common.BaseEntity;
import com.zerobase.everycampingbackend.product.domain.form.ProductManageForm;
import com.zerobase.everycampingbackend.product.type.ProductCategory;
import com.zerobase.everycampingbackend.user.domain.entity.Seller;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

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

    public static Product from(ProductManageForm form){
        return Product.builder()
            .name(form.getName())
            .category(form.getCategory())
            .price(form.getPrice())
            .onSale(form.getOnSale())
            .stock(form.getStock())
            .description(form.getDescription())
            .imagePath(form.getImagePath())
            .detailImagePath(form.getDetailImagePath())
            .tags(form.getTags())
            .build();
    }

    public void setFrom(ProductManageForm form){
        setName(form.getName());
        setCategory(form.getCategory());
        setPrice(form.getPrice());
        setStock(form.getStock());
        setOnSale(form.getOnSale());
        setDescription(form.getDescription());
        setImagePath(form.getImagePath());
        setDetailImagePath(form.getDetailImagePath());
        setTags(form.getTags());
    }
}
