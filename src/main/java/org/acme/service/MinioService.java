package org.acme.service;

import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@ApplicationScoped
public class MinioService {

    @Inject
    MinioClient minioClient;

    @ConfigProperty(name = "minio.bucket.name")
    String bucketName;

    public void createBucketIfNotExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                System.out.println("Bucket '" + bucketName + "' criado com sucesso!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar bucket no MinIO: " + e.getMessage(), e);
        }
    }

    public String uploadFile(String objectName, byte[] fileData, String contentType) {
        try {
            createBucketIfNotExists();

            InputStream inputStream = new ByteArrayInputStream(fileData);

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, fileData.length, -1)
                    .contentType(contentType)
                    .build()
            );

            // Retorna o nome do objeto para referência
            return objectName;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo para o MinIO: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo do MinIO: " + e.getMessage(), e);
        }
    }

    public String getFileUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(60 * 60 * 24) // 24 horas
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL do arquivo: " + e.getMessage(), e);
        }
    }
}