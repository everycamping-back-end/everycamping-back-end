package com.zerobase.everycampingbackend.domain.staticimage.service;

import com.zerobase.everycampingbackend.common.fileutil.ImageUtils;
import com.zerobase.everycampingbackend.domain.staticimage.client.AwsS3Client;
import com.zerobase.everycampingbackend.domain.staticimage.dto.S3Path;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskRejectedException;
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
    @Value("${aws.s3.endpointurl}")
    private String endPointUrl;

    private S3Path uploadFileWithUUID(MultipartFile multipartFile)
        throws IOException, TaskRejectedException {

        String filePath = IMG_DIR_PATH + "/" + ImageUtils.generateFilename(multipartFile);

        //awsS3Client.uploadFileWithUUID(filePath, multipartFile);

        return new S3Path(endPointUrl + "/" + filePath, filePath);
    }

    public S3Path saveImage(MultipartFile multipartFile) throws IOException, TaskRejectedException {
        if(!ImageUtils.validateMultipartFile(multipartFile)){
            return new S3Path("", "");
        }

        return uploadFileWithUUID(multipartFile);
    }

    public S3Path editImage(String bucketFilePath, MultipartFile multipartFile)
        throws IOException, TaskRejectedException {
        if (StringUtils.hasText(bucketFilePath)) {
            //awsS3Client.deleteFile(bucketFilePath);
        }
        if(!ImageUtils.validateMultipartFile(multipartFile)){
            return new S3Path("", "");
        }
        return uploadFileWithUUID(multipartFile);
    }

    public void deleteImage(String bucketFilePath) {
        if (ObjectUtils.isEmpty(bucketFilePath)) {
            return;
        }
        awsS3Client.deleteFile(bucketFilePath);
    }

}
