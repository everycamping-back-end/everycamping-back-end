package com.zerobase.everycampingbackend.order.domain.repository;


import static com.zerobase.everycampingbackend.order.domain.entity.QOrderProduct.orderProduct;
import static com.zerobase.everycampingbackend.order.domain.entity.QOrders.orders;
import static com.zerobase.everycampingbackend.product.domain.entity.QProduct.product;
import static com.zerobase.everycampingbackend.user.domain.entity.QCustomer.customer;
import static com.zerobase.everycampingbackend.user.domain.entity.QSeller.seller;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.everycampingbackend.order.domain.dto.OrderProductByCustomerDto;
import com.zerobase.everycampingbackend.order.domain.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.order.domain.form.SearchOrderByCustomerForm;
import com.zerobase.everycampingbackend.order.domain.form.SearchOrderBySellerForm;
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

        List<OrderProductByCustomerDto> list = queryFactory
            .select(Projections.fields(OrderProductByCustomerDto.class,
                product.id.as("productId"),
                product.name.as("productName"),
                product.price.as("stockPrice"),
                product.imagePath.as("imagePath"),
                orders.id.as("orderId"),
                orderProduct.quantity,
                orderProduct.amount,
                orderProduct.status,
                orderProduct.createdAt,
                seller.id.as("sellerId"),
                seller.nickName.as("sellerNickName")))

            .from(orderProduct)
            .innerJoin(orderProduct.orders, orders)
            .innerJoin(orders.customer, customer)
            .innerJoin(orderProduct.product, product)
            .innerJoin(product.seller, seller)

            .where(
                customer.id.eq(customerId),
                likeProductName(form.getProductName()),
                goe(form.getStartDate()),
                loe(form.getEndDate())
            )

            .orderBy(orderProduct.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = getByCustomerCount(form, customerId);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<OrderProductBySellerDto> searchBySeller(SearchOrderBySellerForm form, Long sellerId,
        Pageable pageable) {

        List<OrderProductBySellerDto> list = queryFactory
            .select(Projections.fields(OrderProductBySellerDto.class,
                product.id.as("productId"),
                product.name.as("productName"),
                product.price.as("stockPrice"),
                product.imagePath.as("imagePath"),
                orders.id.as("orderId"),
                orderProduct.quantity,
                orderProduct.amount,
                orderProduct.status,
                orderProduct.createdAt,
                customer.id.as("customerId"),
                customer.nickName.as("customerNickName")))

            .from(orderProduct)
            .innerJoin(orderProduct.orders, orders)
            .innerJoin(orders.customer, customer)
            .innerJoin(orderProduct.product, product)
            .innerJoin(product.seller, seller)


            .where(
                seller.id.eq(sellerId),
                likeProductName(form.getProductName()),
                goe(form.getStartDate()),
                loe(form.getEndDate())
            )

            .orderBy(orderProduct.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = getBySellerCount(form, sellerId);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);

    }


    private JPAQuery<Long> getByCustomerCount(SearchOrderByCustomerForm form, Long customerId) {
        return queryFactory
            .select(orderProduct.count())
            .from(orderProduct)
            .innerJoin(orderProduct.orders, orders)
            .innerJoin(orders.customer, customer)

            .where(
                customer.id.eq(customerId),
                likeProductName(form.getProductName()),
                goe(form.getStartDate()),
                loe(form.getEndDate())
            );
    }

    private JPAQuery<Long> getBySellerCount(SearchOrderBySellerForm form, Long sellerId) {
        return queryFactory
            .select(orderProduct.count())
            .from(orderProduct)
            .innerJoin(orderProduct.product, product)
            .innerJoin(product.seller, seller)
            .where(
                seller.id.eq(sellerId),
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
