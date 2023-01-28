package com.zerobase.everycampingbackend.domain.settlement.repository;


import static com.zerobase.everycampingbackend.domain.order.entity.QOrderProduct.orderProduct;
import static com.zerobase.everycampingbackend.domain.order.entity.QOrders.orders;
import static com.zerobase.everycampingbackend.domain.user.entity.QCustomer.customer;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.everycampingbackend.common.QueryDslUtil;
import com.zerobase.everycampingbackend.domain.settlement.dto.DailySettlementDetailDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DailySettlementRepositoryImpl implements DailySettlementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DailySettlementDetailDto> getSettlementDetail(Long dailySettlementId,
        Pageable pageable) {

        List<OrderSpecifier> orderByList = getAllOrderSpecifiers(pageable);

        List<DailySettlementDetailDto> list = queryFactory
            .select(Projections.fields(DailySettlementDetailDto.class,

                orderProduct.product.id.as("productId"),
                orderProduct.productNameSnapshot,
                orderProduct.quantity,
                orderProduct.amount,

                customer.id.as("customerId"),
                customer.email.as("customerEmail"),
                customer.nickName.as("customerNickName"),

                orderProduct.createdAt.as("orderedAt"),
                orderProduct.confirmedAt))

            .from(orderProduct)
            .innerJoin(orderProduct.orders, orders)
            .innerJoin(orders.customer, customer)

            .where(
                orderProduct.dailySettlement.id.eq(dailySettlementId)
//                orderProduct.status.eq(OrderStatus.SETTLEMENT)
            )

            .orderBy(orderByList.toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = getDetailCount(dailySettlementId);

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getDetailCount(Long dailySettlementId) {
        return queryFactory
            .select(orderProduct.count())
            .from(orderProduct)
            .where(
                orderProduct.dailySettlement.id.eq(dailySettlementId)
            );
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> orderByList = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "amount":
                        OrderSpecifier<?> orderAmount = QueryDslUtil.getSortedColumn(direction,
                            orderProduct, "amount");
                        orderByList.add(orderAmount);
                        break;

                    case "customerEmail":
                        OrderSpecifier<?> orderEmail = QueryDslUtil.getSortedColumn(direction,
                            customer, "email");
                        orderByList.add(orderEmail);
                        break;

                    case "orderedAt":
                        OrderSpecifier<?> orderOrderedAt = QueryDslUtil.getSortedColumn(direction,
                            orderProduct, "createdAt");
                        orderByList.add(orderOrderedAt);
                        break;

                    default:
                        break;
                }
            }
        }

        return orderByList;
    }

}
