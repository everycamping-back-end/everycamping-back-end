package com.zerobase.everycampingbackend.product.domain.form;

import com.zerobase.everycampingbackend.product.type.ProductCategory;
import java.util.List;
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
public class ProductSearchForm {
    private String name;
    private ProductCategory category;
    private List<String> tags;
}
