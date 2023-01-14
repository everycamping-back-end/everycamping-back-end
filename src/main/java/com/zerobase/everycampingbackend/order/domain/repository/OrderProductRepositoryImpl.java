package com.zerobase.everycampingbackend.order.domain.repository;


import static com.zerobase.everycampingbackend.order.domain.entity.QOrderProduct.orderProduct;
import static com.zerobase.everycampingbackend.product.domain.entity.QProduct.product;
import static com.zerobase.everycampingbackend.user.domain.entity.QSeller.seller;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.everycampingbackend.order.domain.dto.OrderProductByCustomerDto;
import com.zerobase.everycampingbackend.order.domain.form.SearchOrderByCustomerForm;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderProductRepositoryImpl implements OrderProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderProductByCustomerDto> searchByCustomer(SearchOrderByCustomerForm form,
        Long customerId, Pageable pageable) {
        System.out.println(form.getStartDate());
        System.out.println(form.getEndDate());
        List<OrderProductByCustomerDto> list = queryFactory
            .select(Projections.fields(OrderProductByCustomerDto.class,
                product.id.as("productId"),
                product.name.as("productName"),
                product.price.as("stockPrice"),
                product.imagePath.as("imagePath"),
                orderProduct.quantity,
                orderProduct.amount,
                orderProduct.status,
                orderProduct.createdAt,
                seller.id.as("sellerId"),
                seller.nickName.as("sellerNickName")))

            .from(orderProduct)
//            .leftJoin(orderProduct.orders, orders)
            .innerJoin(orderProduct.product, product)
            .innerJoin(product.seller, seller)

            .where(
                orderProduct.orders.customer.id.eq(customerId),
                likeProductName(form.getProductName()),
                goe(form.getStartDate()),
                loe(form.getEndDate())
            )

            .orderBy(orderProduct.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = getCount(form, customerId);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCount(SearchOrderByCustomerForm form, Long customerId) {
        return queryFactory
            .select(orderProduct.count())
            .from(orderProduct)
            .where(
                orderProduct.orders.customer.id.eq(customerId),
                likeProductName(form.getProductName()),
                goe(form.getStartDate()),
                loe(form.getEndDate())
            );
    }

    private BooleanExpression likeProductName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            return null;
        }

        return product.name.like("%" + name + "%");
    }

    private BooleanExpression goe(Date startDate) {
        if (startDate == null) {
            return null;
        }

        return orderProduct.createdAt.goe(
            LocalDateTime.of(new java.sql.Date(startDate.getTime()).toLocalDate(), LocalTime.MIN));
    }

    private BooleanExpression loe(Date endDate) {
        if (endDate == null) {
            return null;
        }

        return orderProduct.createdAt.loe(
            LocalDateTime.of(new java.sql.Date(endDate.getTime()).toLocalDate(), LocalTime.MAX)
                .withNano(0));
    }
}
