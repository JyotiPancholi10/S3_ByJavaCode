package aws.s3.controller;

import aws.s3.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class UploadController {

    @Autowired
    private S3UploadService s3UploadService;

    @PostMapping("/upload-logs")
    public String uploadLogs() {
        String bucketName = "jiyas3bucket";
        String filePath = "logs/app.log";
        String keyName = "logs/app-" + System.currentTimeMillis() + ".log";

        File file = new File(filePath);
        if (!file.exists()) {
            return "Log file does not exist: " + filePath;
        }
        if (file.isDirectory()) {
            return "Expected a file but found a directory at: " + filePath;
        }

        s3UploadService.uploadFile(bucketName, filePath, keyName);
        return "Upload initiated.";
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String key) {
        String bucketName = "jiyas3bucket";

        byte[] fileData = s3UploadService.downloadFile(bucketName, key);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(key).build());

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }
}