package com.zerobase.everycampingbackend.domain.admin.service;

import com.zerobase.everycampingbackend.domain.admin.dto.SellerRequestDto;
import com.zerobase.everycampingbackend.domain.admin.entity.SellerRequest;
import com.zerobase.everycampingbackend.domain.admin.repository.SellerRequestRepository;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import com.zerobase.everycampingbackend.domain.user.repository.SellerRepository;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerRequestService {

    private final SellerRequestRepository sellerRequestRepository;
    private final SellerRepository sellerRepository;

    public void applySellerRequest(Seller seller) {
        sellerRequestRepository.save(SellerRequest.from(seller));
    }

    public Page<SellerRequestDto> getSellerRequests(Pageable pageable) {
        return new PageImpl<>(
            sellerRequestRepository.findAll(pageable)
                .stream().map(SellerRequestDto::from)
                .collect(Collectors.toList()));
    }

    public void confirmSellerRequests(List<Long> ids){
        List<SellerRequest> list = new ArrayList<>();

        for(Long id : ids){
            list.add(sellerRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SELLER_REQUEST_NOT_FOUND)));
        }

        if(list.isEmpty()){
            return;
        }

        List<Seller> sellers = new ArrayList<>();
        for(SellerRequest request : list){
            Seller seller = request.getSeller();
            seller.setConfirmed(true);
            sellers.add(seller);
        }

        sellerRepository.saveAll(sellers);
        sellerRequestRepository.deleteAll(list);
    }
}
