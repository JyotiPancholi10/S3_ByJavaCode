package aws.s3.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class S3UploadService {
    private final S3Client s3Client;

    public S3UploadService() {
        this.s3Client = S3Client.builder()
                .region(Region.of("ap-south-1")) // choose your region
                .credentialsProvider(ProfileCredentialsProvider.create()) // uses ~/.aws/credentials
                .build();
    }

    public void uploadFile(String bucketName, String filePath, String keyName) {
        try {
            Path path = Paths.get(filePath);

            if (!path.toFile().exists() || path.toFile().isDirectory()) {
                System.err.println("The file " + filePath + " does not exist or is a directory.");
                return;
            }

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.putObject(request, RequestBody.fromFile(path));
            System.out.println("Uploaded file to S3: " + keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void uploadFile(String bucketName, String filePath, String keyName) {
//        try {
//            Path path = Paths.get(filePath);
//            PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(keyName)
//                    .build();
//
//            s3Client.putObject(request, RequestBody.fromFile(path));
//            System.out.println("Uploaded file to S3: " + keyName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public byte[] downloadFile(String bucketName, String keyName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = s3Object.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to download file: " + keyName);
        }
    }
}
