package com.zerobase.everycampingbackend.product.domain.form;

import com.zerobase.everycampingbackend.product.type.ProductCategory;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductManageForm {

    @NotNull
    private ProductCategory category;
    @NotBlank
    private String name;
    @NotNull
    private Integer price;
    @NotNull
    private Boolean onSale;

    private String description;
    private Integer stock;
    private String imagePath;
    private String detailImagePath;
    private List<String> tags;
}
