package com.zerobase.everycampingbackend.domain.staticimage.service;

import com.zerobase.everycampingbackend.domain.staticimage.client.AwsS3Client;
import com.zerobase.everycampingbackend.domain.staticimage.dto.S3Path;
import com.zerobase.everycampingbackend.domain.staticimage.type.ImageFormat;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaticImageService {

    private final AwsS3Client awsS3Client;

    private static final String IMG_DIR_PATH = "img";

    public S3Path saveImage(MultipartFile multipartFile) throws IOException {
        if(ObjectUtils.isEmpty(multipartFile)){
            return new S3Path("", "");
        }

        int pos = Objects.requireNonNull(multipartFile.getOriginalFilename()).lastIndexOf(".");
        String extension = multipartFile.getOriginalFilename().substring(pos + 1);
        if(ObjectUtils.isEmpty(ImageFormat.getExtension(extension))){
            return new S3Path("", "");
        }

        return awsS3Client.uploadFileWithUUID(multipartFile, IMG_DIR_PATH);
    }

    public void deleteImage(String bucketFilePath) {
        if(ObjectUtils.isEmpty(bucketFilePath)){
            return;
        }
        awsS3Client.deleteFile(bucketFilePath);
    }

    public S3Path editImage(String bucketFilePath, MultipartFile multipartFile) throws IOException {
        if(StringUtils.hasText(bucketFilePath)){
            awsS3Client.deleteFile(bucketFilePath);
        }
        if(ObjectUtils.isEmpty(multipartFile)){
            return new S3Path("", "");
        }
        return awsS3Client.uploadFileWithUUID(multipartFile, IMG_DIR_PATH);
    }
}
