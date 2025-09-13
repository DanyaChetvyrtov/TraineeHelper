package ru.core.profilems.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PhotoStorageService {
    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucketName;

    public void uploadPhoto(String profileId, MultipartFile file) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(profileId)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    public byte[] downloadPhoto(String profileId) throws Exception {
        try(var inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(profileId)
                        .build()
        )){
            return inputStream.readAllBytes();
        }
    }

    public void deletePhoto(String profileId) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(profileId)
                        .build()
        );
    }
}