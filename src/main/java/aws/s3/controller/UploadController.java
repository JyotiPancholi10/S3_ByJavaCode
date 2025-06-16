package aws.s3.controller;

import aws.s3.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {

    @Autowired
    private S3UploadService s3UploadService;

    @PostMapping("/upload-logs")
    public String uploadLogs() {
        String bucketName = "jiyas3bucket";
        String filePath = "logs/app.log";
        String keyName = "logs/app-" + System.currentTimeMillis() + ".log";

        s3UploadService.uploadFile(bucketName, filePath, keyName);
        return "Upload initiated.";
    }
}