package com.zerobase.everycampingbackend.domain.order.repository;


import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.zerobase.everycampingbackend.domain.order.entity.QOrderProduct.orderProduct;
import static com.zerobase.everycampingbackend.domain.order.entity.QOrders.orders;

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
    public OrderDetailByCustomerDto getOrderDetailByCustomer(Long orderId) {

        List<OrderDetailByCustomerDto> list = queryFactory.selectFrom(orders)
            .join(orderProduct).on(orderProduct.orders.id.eq(orders.id))
            .where(orders.id.eq(orderId))
            .distinct()
            .transform(
                groupBy(orders.id).list(
                    Projections.constructor(OrderDetailByCustomerDto.class, list(
                            Projections.constructor(OrderProductDto.class,
                                orderProduct.id, orderProduct.product.id,
                                orderProduct.productNameSnapshot,
                                orderProduct.stockPriceSnapshot, orderProduct.amount,
                                orderProduct.imageUriSnapshot
                                , orderProduct.quantity, orderProduct.status))
                        , orders.id.as("orderId"), orders.representProductName,
                        orders.orderProductCount
                        , orders.totalAmount, orders.name, orders.address, orders.phone,
                        orders.request
                        , orders.createdAt
                    ))
            );

        return list.get(0);
    }
}
