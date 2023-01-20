package com.zerobase.everycampingbackend.domain.order.repository;


import static com.zerobase.everycampingbackend.domain.order.entity.QOrderProduct.orderProduct;
import static com.zerobase.everycampingbackend.domain.order.entity.QOrders.orders;
import static com.zerobase.everycampingbackend.domain.product.entity.QProduct.product;
import static com.zerobase.everycampingbackend.domain.user.entity.QCustomer.customer;
import static com.zerobase.everycampingbackend.domain.user.entity.QSeller.seller;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.everycampingbackend.common.QueryDslUtil;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.domain.order.form.SearchOrderByCustomerForm;
import com.zerobase.everycampingbackend.domain.order.form.SearchOrderBySellerForm;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderProductRepositoryImpl implements OrderProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderProductByCustomerDto> searchByCustomer(SearchOrderByCustomerForm form,
        Long customerId, Pageable pageable) {

        List<OrderSpecifier> orderByList = getAllOrderSpecifiers(pageable);

        List<OrderProductByCustomerDto> list = queryFactory
            .select(Projections.fields(OrderProductByCustomerDto.class,
                orderProduct.product.id.as("productId"),
                orderProduct.productNameSnapshot,
                orderProduct.stockPriceSnapshot,
                orderProduct.imageUriSnapshot,
                orderProduct.id.as("orderProductId"),
                orderProduct.quantity,
                orderProduct.amount,
                orderProduct.status,
                orderProduct.createdAt))

            .from(orderProduct)
            .innerJoin(orderProduct.orders, orders)

            .where(
                orders.customer.id.eq(customerId),
                likeProductName(form.getProductName()),
                goe(form.getStartDate()),
                loe(form.getEndDate())
            )

            .orderBy(orderByList.toArray(OrderSpecifier[]::new))
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
                orderProduct.id.as("orderProductId"),
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

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "amount":
                        OrderSpecifier<?> orderAmount = QueryDslUtil.getSortedColumn(direction,
                            orderProduct.amount, "amount");
                        ORDERS.add(orderAmount);
                        break;
                    case "createdAt":
                        OrderSpecifier<?> orderCreatedAt = QueryDslUtil.getSortedColumn(direction,
                            orderProduct.createdAt, "createdAt");
                        ORDERS.add(orderCreatedAt);
                        break;
                    case "status":
                        OrderSpecifier<?> orderStatus = QueryDslUtil.getSortedColumn(direction,
                            orderProduct.status, "status");
                        ORDERS.add(orderStatus);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
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
