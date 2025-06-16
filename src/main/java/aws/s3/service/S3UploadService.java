package aws.s3.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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
}
