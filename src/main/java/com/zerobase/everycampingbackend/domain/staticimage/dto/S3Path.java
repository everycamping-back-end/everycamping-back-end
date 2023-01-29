package com.zerobase.everycampingbackend.domain.staticimage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class S3Path {
    private String imageUri;
    private String imagePath;
}
