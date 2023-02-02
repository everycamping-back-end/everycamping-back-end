package com.zerobase.everycampingbackend.domain.product.repository;

import static com.zerobase.everycampingbackend.domain.product.entity.QProduct.product;
import static com.zerobase.everycampingbackend.domain.user.entity.QSeller.seller;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.everycampingbackend.common.QueryDslUtil;
import com.zerobase.everycampingbackend.domain.product.dto.ProductDto;
import com.zerobase.everycampingbackend.domain.product.form.ProductSearchForm;
import com.zerobase.everycampingbackend.domain.product.type.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ProductDto> searchAll(ProductSearchForm form, Pageable pageable) {
//        return new SliceImpl<>(searchAllNonTuned(form, pageable));
        return new SliceImpl<>(searchAllTuned(form, pageable));
    }

    private List<ProductDto> searchAllTuned(ProductSearchForm form, Pageable pageable) {
        List<Long> ids = queryFactory.select(product.id)
            .from(product)
            .where(
                eqCategory(form.getCategory()),
                containTags(form.getTags()),
                likeName(form.getName())
            )
            .orderBy(getAllOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<ProductDto> products = new ArrayList<>();
        for(Long id : ids) {
            List<ProductDto> tmp = queryFactory.select(Projections.fields(ProductDto.class,
                    product.id,
                    seller.nickName.as("sellerName"),
                    product.category,
                    product.name,
                    product.price,
                    product.imageUri,
                    product.reviewCount,
                    product.avgScore,
                    product.createdAt,
                    product.modifiedAt))
                .from(product)
                .innerJoin(product.seller, seller)
                .where(product.id.eq(id))
//                .orderBy(getAllOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
//                .offset(pageable.getOffset())
                .limit(1)
                .fetch();

            products.addAll(tmp);
        }

        return products;
    }

    private List<ProductDto> searchAllNonTuned(ProductSearchForm form, Pageable pageable) {
        List<ProductDto> products = queryFactory.select(Projections.fields(ProductDto.class,
                product.id,
                seller.nickName.as("sellerName"),
                product.category,
                product.name,
                product.price,
                product.imageUri,
                product.reviewCount,
                product.avgScore,
                product.createdAt,
                product.modifiedAt))
            .from(product)
            .innerJoin(product.seller, seller)
            .where(
                eqCategory(form.getCategory()),
                containTags(form.getTags()),
                likeName(form.getName())
            )
            .orderBy(getAllOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return products;
    }

    private BooleanBuilder containTags(List<String> tags) {
        if (ObjectUtils.isEmpty(tags)) {
            return null;
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String tag : tags) {
            booleanBuilder.and(product.tags.contains(tag));
        }
        return booleanBuilder;
    }

    private BooleanExpression eqCategory(ProductCategory category) {
        if (ObjectUtils.isEmpty(category)) {
            return null;
        }
        return product.category.eq(category);
    }

    private BooleanExpression likeName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            return null;
        }

        return product.name.like("%" + name + "%");
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> orderByList = new ArrayList<>();

        if (!isEmpty(pageable.getSort()) && pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                if ("createdAt".equals(order.getProperty())) {
                    orderByList.add(QueryDslUtil.getSortedColumn(direction,
                        product.createdAt, "createdAt"));
                } else if ("price".equals(order.getProperty())){
                    orderByList.add(QueryDslUtil.getSortedColumn(direction,
                        product.price, "price"));
                } else if ("avgScore".equals(order.getProperty())){
                    orderByList.add(QueryDslUtil.getSortedColumn(direction,
                        product.avgScore, "avgScore"));
                } else if ("id".equals(order.getProperty())){
                    orderByList.add(QueryDslUtil.getSortedColumn(direction,
                        product.id, "id"));
                }
            }
        } else {
            orderByList.add(QueryDslUtil.getSortedColumn(Order.DESC,
                product.id, "id"));
        }

        return orderByList;
    }
}
