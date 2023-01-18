package com.zerobase.everycampingbackend.domain.review.entity;

import com.zerobase.everycampingbackend.common.BaseEntity;
import com.zerobase.everycampingbackend.domain.staticimage.dto.S3Path;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.review.form.ReviewForm;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import javax.persistence.Entity;
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
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer score;
    private String text;

    private String imageUri;
    private String imagePath;


    public static Review of(ReviewForm form, Customer customer, Product product, S3Path s3Path) {
        return Review.builder()
            .customer(customer)
            .product(product)
            .score(form.getScore())
            .text(form.getText())
            .imageUri(s3Path.getImageUri())
            .imagePath(s3Path.getImagePath())
            .build();
    }

    public void setOf(ReviewForm form, S3Path s3Path) {
        score = form.getScore();
        text = form.getText();
        imageUri = s3Path.getImageUri();
        imagePath = s3Path.getImagePath();
    }
}
