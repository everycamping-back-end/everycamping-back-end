package com.zerobase.everycampingbackend.domain.order.repository;


import static com.zerobase.everycampingbackend.domain.order.entity.QOrderProduct.orderProduct;
import static com.zerobase.everycampingbackend.domain.order.entity.QOrders.orders;
import static com.zerobase.everycampingbackend.domain.product.entity.QProduct.product;
import static com.zerobase.everycampingbackend.domain.user.entity.QCustomer.customer;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.everycampingbackend.common.QueryDslUtil;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductBySellerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderProductDetailBySellerDto;
import com.zerobase.everycampingbackend.domain.order.form.GetOrderProductBySellerForm;
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
    public Page<OrderProductBySellerDto> getOrderProductsBySeller(GetOrderProductBySellerForm form,
        Long sellerId, Pageable pageable) {

        List<OrderSpecifier> orderByList = getAllOrderSpecifiers(pageable);

        List<OrderProductBySellerDto> list = queryFactory
            .select(Projections.fields(OrderProductBySellerDto.class,

                orderProduct.id.as("orderProductId"),
                product.id.as("productId"),

                orderProduct.productNameSnapshot,
                orderProduct.stockPriceSnapshot,
                orderProduct.quantity,
                orderProduct.amount,

                orderProduct.createdAt,
                orderProduct.status))

            .from(orderProduct)
            .innerJoin(orderProduct.product, product)

            .where(
                product.seller.id.eq(sellerId),
                goe(form.getStartDate()),
                loe(form.getEndDate())
            )

            .orderBy(orderByList.toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = getBySellerCount(form, sellerId);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    @Override
    public List<OrderProductDetailBySellerDto> getOrderProductDetailBySeller(Long orderProductId) {

        return queryFactory
            .select(Projections.fields(OrderProductDetailBySellerDto.class,

                orderProduct.id.as("orderProductId"),
                product.id.as("productId"),
                orderProduct.productNameSnapshot,
                orderProduct.stockPriceSnapshot,
                orderProduct.imageUriSnapshot,
                orderProduct.quantity,
                orderProduct.amount,

                orders.name.as("receiverName"),
                orders.address.as("receiverAddress"),
                orders.phone.as("receiverPhone"),
                orders.request,

                customer.id.as("customerId"),
                customer.email.as("customerEmail"),
                customer.nickName.as("customerNickName"),
                customer.phone.as("customerPhone"),

                orderProduct.createdAt,
                orderProduct.status,

                product.seller.id.as("sellerId")
                ))

            .from(orderProduct)
            .innerJoin(orderProduct.orders, orders)
            .innerJoin(orders.customer, customer)
            .innerJoin(orderProduct.product, product)

            .where(
                orderProduct.id.eq(orderProductId)
            )
            .fetch();
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> orderByList = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "amount":
                        OrderSpecifier<?> orderAmount = QueryDslUtil.getSortedColumn(direction,
                            orderProduct.amount, "amount");
                        orderByList.add(orderAmount);
                        break;
                    case "createdAt":
                        OrderSpecifier<?> orderCreatedAt = QueryDslUtil.getSortedColumn(direction,
                            orderProduct.createdAt, "createdAt");
                        orderByList.add(orderCreatedAt);
                        break;
                    case "status":
                        OrderSpecifier<?> orderStatus = QueryDslUtil.getSortedColumn(direction,
                            orderProduct.status, "status");
                        orderByList.add(orderStatus);
                        break;
                    default:
                        break;
                }
            }
        }

        return orderByList;
    }

    private JPAQuery<Long> getBySellerCount(GetOrderProductBySellerForm form, Long sellerId) {
        return queryFactory
            .select(orderProduct.count())
            .from(orderProduct)
            .innerJoin(orderProduct.product, product)
            .where(
                product.seller.id.eq(sellerId),
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

    private BooleanExpression likeProductNameSnapShot(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            return null;
        }

        return orderProduct.productNameSnapshot.like("%" + name + "%");
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
