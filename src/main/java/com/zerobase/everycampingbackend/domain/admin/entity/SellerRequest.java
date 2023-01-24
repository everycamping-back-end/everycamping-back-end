package com.zerobase.everycampingbackend.domain.admin.entity;

import com.zerobase.everycampingbackend.common.BaseEntity;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    public static SellerRequest from(Seller seller){
        return SellerRequest.builder()
            .seller(seller)
            .build();
    }
}
