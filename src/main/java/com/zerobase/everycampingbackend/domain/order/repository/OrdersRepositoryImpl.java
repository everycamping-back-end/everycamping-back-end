package com.zerobase.everycampingbackend.domain.order.repository;


import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.zerobase.everycampingbackend.domain.order.entity.QOrderProduct.orderProduct;
import static com.zerobase.everycampingbackend.domain.order.entity.QOrders.orders;
import static com.zerobase.everycampingbackend.domain.product.entity.QProduct.product;
import static com.zerobase.everycampingbackend.domain.user.entity.QSeller.seller;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.everycampingbackend.domain.order.dto.OrderDetailByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.dto.OrderDetailByCustomerDto.OrderProductDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderDetailByCustomerDto> getOrderDetailByCustomer(Long orderId) {

        return queryFactory.selectFrom(orders)
            .innerJoin(orderProduct).on(orderProduct.orders.id.eq(orders.id))
            .innerJoin(orderProduct.product, product)
            .innerJoin(product.seller, seller)
            .where(orders.id.eq(orderId))
            .transform(
                groupBy(orders.id).list(
                    Projections.constructor(OrderDetailByCustomerDto.class, list(
                            Projections.constructor(OrderProductDto.class,
                                orderProduct.id, orderProduct.product.id,
                                orderProduct.productNameSnapshot,
                                orderProduct.stockPriceSnapshot, orderProduct.amount,
                                orderProduct.imageUriSnapshot
                                , orderProduct.quantity, orderProduct.status
                                , seller.id, seller.nickName
                                , seller.email, seller.phone
                            ))
                        , orders.id.as("orderId"), orders.representProductName,
                        orders.orderProductCount
                        , orders.totalAmount, orders.name, orders.address, orders.phone,
                        orders.request
                        , orders.customer.id
                        , orders.createdAt
                    ))
            );
    }
}
