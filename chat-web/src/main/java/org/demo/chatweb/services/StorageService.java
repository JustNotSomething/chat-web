package org.demo.chatweb.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


@Service
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;
    public String uploadFile(MultipartFile file, int userId)
    {
        File fileObj = convertMultiplePartFileToFile(file);
        String filename = "avatars/avatar_" + userId;
        s3Client.putObject(new PutObjectRequest(bucketName, filename, fileObj));
        fileObj.delete();
        return "File Uploaded: " + filename;
    }

    public byte[] downloadFile(String filename) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte [] content = IOUtils.toByteArray(inputStream);
        return content;
    }


    public String deleteFile(String filename)
    {
        s3Client.deleteObject(bucketName, filename);
        return filename + "removed...";
    }


    private File convertMultiplePartFileToFile(MultipartFile file)
    {
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }
}
