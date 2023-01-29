package com.zerobase.everycampingbackend.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.everycampingbackend.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private String customerName;
    private Integer score;
    private String text;
    private String imageUri;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public static ReviewDto from(Review review){
        return ReviewDto.builder()
            .id(review.getId())
            .customerName(review.getCustomer().getNickName())
            .score(review.getScore())
            .text(review.getText())
            .imageUri(review.getImageUri())
            .createdAt(review.getCreatedAt())
            .modifiedAt(review.getModifiedAt())
            .build();
    }
}
