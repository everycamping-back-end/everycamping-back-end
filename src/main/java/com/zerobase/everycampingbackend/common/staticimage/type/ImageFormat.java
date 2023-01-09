package com.zerobase.everycampingbackend.common.staticimage.type;

import java.util.Arrays;

public enum ImageFormat {
    JPEG, JPG, PNG, WEBP;

    public static ImageFormat getExtension(String str){
        return Arrays.stream(values())
            .filter(e -> e.name().equals(str.toUpperCase()))
            .findFirst()
            .orElse(null);
    }
}
