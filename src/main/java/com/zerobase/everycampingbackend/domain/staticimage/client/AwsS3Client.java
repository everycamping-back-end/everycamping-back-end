package com.zerobase.everycampingbackend.domain.staticimage.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zerobase.everycampingbackend.domain.staticimage.dto.S3Path;
import java.io.File;
import java.io.FileOutputStream;
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

    public S3Path uploadFileWithUUID(MultipartFile multipartFile, String bucketDirPath) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        String filePath = bucketDirPath + "/" + generateFilename(file);

//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType(multipartFile.getContentType());
//        objectMetadata.setContentLength(multipartFile.getInputStream().available());
//        amazonS3.putObject(bucketName, filePath, multipartFile.getInputStream(), objectMetadata);

        amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file)
            .withCannedAcl(CannedAccessControlList.PublicRead));

        return new S3Path(endPointUrl + "/" + filePath, filePath);
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());
        outputStream.close();
        return file;
    }

    private String generateFilename(File file){
        String datetimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        String uuid = datetimeStr + "-" + UUID.randomUUID();
        String fileName = Objects.requireNonNull(file.getName()).replace(" ", "-");

        return uuid + "-" + fileName;
    }

    public void deleteFile(String bucketFilePath) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, bucketFilePath));
    }
}
