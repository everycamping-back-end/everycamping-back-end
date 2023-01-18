package com.zerobase.everycampingbackend.domain.review.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewForm {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;
    @NotBlank
    @Size(min = 10)
    private String text;
//    private MultipartFile image;
}
