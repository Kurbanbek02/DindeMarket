package com.dindeMarket.config.s3service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
public class AwsFileService implements FileService {

    private final S3Client s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    public AwsFileService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = UUID.randomUUID().toString() + "." + extension;

        try {
            // Определяем MIME-тип файла
            String contentType = file.getContentType();

            // Создаем запрос для загрузки объекта с MIME-типом
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)  // Устанавливаем Content-Type
                    //.acl("public-read") // Устанавливаем права доступа к объекту
                    .build();

            // Загружаем файл в S3
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Возвращаем URL загруженного файла
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading the file", e);
        } catch (S3Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interacting with S3", e);
        }
    }
}

//    @Override
//    public String deleteFile(String fileName) {
//        s3Client.deleteObject(bucketName, fileName);
//        return fileName + " File successfully deleted!";
//    }
//
//    @Override
//    public byte[] downloadFile(String fileName) {
//        S3Object s3Object = s3Client.getObject(bucketName, fileName);
//        S3ObjectInputStream inputStream = s3Object.getObjectContent();
//        try {
//            byte[] content = IOUtils.toByteArray(inputStream);
//            return content;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

