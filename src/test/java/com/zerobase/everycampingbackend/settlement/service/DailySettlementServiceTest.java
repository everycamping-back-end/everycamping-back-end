package com.zerobase.everycampingbackend.settlement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zerobase.everycampingbackend.domain.settlement.dto.DailySettlementDto;
import com.zerobase.everycampingbackend.domain.settlement.entity.DailySettlement;
import com.zerobase.everycampingbackend.domain.settlement.form.GetDailySettlementsForm;
import com.zerobase.everycampingbackend.domain.settlement.repository.DailySettlementRepository;
import com.zerobase.everycampingbackend.domain.settlement.service.DailySettlementService;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import com.zerobase.everycampingbackend.domain.user.repository.SellerRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class DailySettlementServiceTest {

    @Autowired
    DailySettlementService dailySettlementService;
    @Autowired
    DailySettlementRepository dailySettlementRepository;
    @Autowired
    SellerRepository sellerRepository;

    @Test
    @DisplayName("일간정산 목록 조회 성공")
    void orderSuccess() throws Exception {

        //given
        Seller seller = createSeller();
        createSettlement(1111L, seller);
        createSettlement(2222L, seller);
        createSettlement(3333L, seller);
        createSettlement(4444L, seller);

        PageRequest pageRequest = PageRequest.of(0, 5);
        GetDailySettlementsForm form = GetDailySettlementsForm.builder().build();

        //when
        Page<DailySettlementDto> result = dailySettlementService.getSettlements(form, seller,
            pageRequest);

        //then
        List<DailySettlementDto> content = result.getContent();
        assertEquals(4, content.size());

        for(int i=1; i<=4; i++) {
            assertEquals(i*1111, content.get(i-1).getAmount());
        }
    }


    private void createSettlement(Long amount, Seller seller) {
        dailySettlementRepository.save(DailySettlement.builder()
            .seller(seller)
            .amount(amount)
            .targetDate(LocalDate.now())
            .build());
    }

    private Seller createSeller() {
        Seller seller = Seller.builder()
            .email("seller@naver.com")
            .nickName("판매자닉네임")
            .build();

        return sellerRepository.save(seller);
    }
}