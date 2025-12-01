package org.acme.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
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

    @ConfigProperty(name = "minio.url")
    String minioUrl;

    public void createBucketIfNotExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                System.out.println("✅ Bucket '" + bucketName + "' criado com sucesso!");
                
                // Configurar bucket como público automaticamente
                configurarBucketPublico();
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar bucket no MinIO: " + e.getMessage());
            throw new RuntimeException("Erro ao criar bucket no MinIO: " + e.getMessage(), e);
        }
    }
    
    private void configurarBucketPublico() {
        try {
            String policyJson = String.format("""
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": "*",
                            "Action": ["s3:GetObject"],
                            "Resource": ["arn:aws:s3:::%s/*"]
                        }
                    ]
                }
                """, bucketName);
            
            minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(policyJson)
                    .build()
            );
            System.out.println("✅ Bucket configurado como público!");
        } catch (Exception e) {
            System.err.println("⚠️  Não foi possível configurar bucket como público: " + e.getMessage());
        }
    }

    public String uploadFile(String objectName, byte[] fileData, String contentType) {
        try {
            createBucketIfNotExists();

            InputStream inputStream = new ByteArrayInputStream(fileData);

            System.out.println("📤 Upload para MinIO: " + objectName + " (" + fileData.length + " bytes)");
            
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, fileData.length, -1)
                    .contentType(contentType)
                    .build()
            );

            System.out.println("✅ Upload concluído: " + objectName);
            return objectName;
            
        } catch (Exception e) {
            System.err.println("❌ Erro no upload para MinIO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao fazer upload do arquivo para o MinIO: " + e.getMessage(), e);
        }
    }

    public String getFileUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(60 * 60 * 24 * 7) // 7 dias
                    .build()
            );
        } catch (Exception e) {
            System.err.println("⚠️  Erro ao gerar URL assinada: " + e.getMessage());
            return getPublicUrl(objectName); // Fallback para URL pública
        }
    }

    // 🔥 URL pública direta (funciona se o bucket for público)
    public String getPublicUrl(String objectName) {
        // Remove "http://" se existir e adiciona novamente para garantir formato correto
        String baseUrl = minioUrl.replaceFirst("^https?://", "");
        return "http://" + baseUrl + "/" + bucketName + "/" + objectName;
    }

    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            System.out.println("🗑️  Arquivo removido: " + objectName);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo do MinIO: " + e.getMessage(), e);
        }
    }
}