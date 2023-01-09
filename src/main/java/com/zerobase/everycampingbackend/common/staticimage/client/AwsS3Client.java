package com.zerobase.everycampingbackend.common.staticimage.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.zerobase.everycampingbackend.common.staticimage.dto.S3Path;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AwsS3Client {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketname}")
    private String bucketName;
    @Value("${aws.s3.endpointurl}")
    private String endPointUrl;

    public S3Path uploadFileWithUUID(MultipartFile file, String bucketDirPath) throws IOException {
        String filePath = bucketDirPath + "/" + generateFilename(file);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());
        amazonS3.putObject(bucketName, filePath, file.getInputStream(), objectMetadata);

        return new S3Path(endPointUrl + "/" + filePath, filePath);
    }

    private String generateFilename(MultipartFile file){
        String datetimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        String uuid = datetimeStr + "-" + UUID.randomUUID();
        String fileName = Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "-");

        return uuid + "-" + fileName;
    }

    public void deleteFile(String bucketFilePath) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, bucketFilePath));
    }
}
