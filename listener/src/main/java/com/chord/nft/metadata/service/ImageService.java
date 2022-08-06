package com.chord.nft.metadata.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Component
public class ImageService {


    AmazonS3 s3client;

    //String bucketName = "ethseoulnft";
    // String filesToUpload = "/Users/narendra/Downloads/bird1.jpg";

    String s3BucketURL = "https://ethseoulnft.s3.ap-southeast-1.amazonaws.com";

    @Value("${AWS_S3_BUCKET}")
    String bucketName;

    @Value("${AWS_S3_ACCESS_KEY}")
    String s3AccessKey;

    @Value("${AWS_S3_SECRET_KEY}")
    String s3SecretKey;

    @PostConstruct
    void initialize() throws Exception {
        AWSCredentials credentials = new BasicAWSCredentials(
                s3AccessKey,
                s3SecretKey
        );

        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }


    public void upload(String filesToUpload) {
        String[] urlParts = filesToUpload.split("/");
        String outputFileName = urlParts[urlParts.length-1];
        s3client.putObject(
                bucketName,
                "images/"+outputFileName,
                new File(filesToUpload)
        );
    }

    public  String download(String fileUrl) throws Exception
    {
        URL url = new URL(fileUrl);
        String[] urlParts = fileUrl.split("/");
        String outputFileName = urlParts[urlParts.length-1];
        String outputFilePath = "/tmp/";
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFilePath + outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        return outputFilePath + outputFileName;
    }


    private static byte[] getObjectFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;
    }
}
