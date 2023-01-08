package com.zerobase.everycampingbackend.product.domain.repository;

import static com.zerobase.everycampingbackend.product.domain.entity.QProduct.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.everycampingbackend.product.domain.dto.ProductDto;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.form.ProductSearchForm;
import com.zerobase.everycampingbackend.product.type.ProductCategory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductDto> searchAll(ProductSearchForm form, Pageable pageable) {
        List<Product> products =  queryFactory.selectFrom(product)
            .where(
                likeName(form.getName()),
                eqCategory(form.getCategory()),
                containTags(form.getTags())
            )
            .orderBy(product.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(products.stream().map(ProductDto::from).collect(Collectors.toList()));
    }

    private BooleanBuilder containTags(List<String> tags) {
        if(ObjectUtils.isEmpty(tags)) {
            return null;
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String tag : tags){
            booleanBuilder.and(product.tags.contains(tag));
        }
        return booleanBuilder;
    }

    private BooleanExpression eqCategory(ProductCategory category) {
        if(ObjectUtils.isEmpty(category)) {
            return null;
        }
        return product.category.eq(category);
    }

    private BooleanExpression likeName(String name) {
        if(StringUtils.isNullOrEmpty(name)) {
            return null;
        }

        return product.name.like("%" + name + "%");
    }
}
