package com.zerobase.everycampingbackend.domain.order.form;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderProductBySellerForm {

    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
