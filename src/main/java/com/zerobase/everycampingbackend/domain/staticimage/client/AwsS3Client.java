package com.zerobase.everycampingbackend.domain.staticimage.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zerobase.everycampingbackend.common.fileutil.ImageUtils;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AwsS3Client {
    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Async
    public void uploadFileWithUUID(String filePath, MultipartFile multipartFile)
        throws IOException, TaskRejectedException {
        File file = ImageUtils.convertMultiPartToFile(multipartFile);
        amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file)
            .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Async
    public void deleteFile(String bucketFilePath) throws TaskRejectedException {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, bucketFilePath));
    }
}
