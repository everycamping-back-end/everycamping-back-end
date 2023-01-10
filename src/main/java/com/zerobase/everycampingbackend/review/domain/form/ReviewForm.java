package com.zerobase.everycampingbackend.review.domain.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ReviewForm {
    @NotNull
    private Long customerId;
    @NotNull
    private Long productId;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;
    @NotBlank
    @Size(min = 10)
    private String text;
    private MultipartFile image;
}
