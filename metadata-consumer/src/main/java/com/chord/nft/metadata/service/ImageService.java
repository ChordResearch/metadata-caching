package com.chord.nft.metadata.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.UUID;

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
        String outputFileName = UUID.randomUUID().toString()+".jpg"; //just assign some random file name
        String outputFilePath = "/tmp/";
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFilePath + outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        return outputFilePath + outputFileName;
    }


    public JSONObject getMetadataFromTokenURI(String tokenURI) throws Exception{
        InputStream is = new URL(tokenURI).openStream();
        JSONObject metadata = new JSONObject();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            metadata = new JSONObject(jsonText);
        } finally {
            is.close();
            return metadata;
        }
    }

    public String getImageFromTokenURI(String tokenURI) throws Exception{
        InputStream is = new URL(tokenURI).openStream();
        String image = null;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            image =  json.getString("image");
            image = formatIpfsURL(image);
        } finally {
            is.close();
            return image;
        }
    }
    private  String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private String formatIpfsURL(String url) {
        return url.replaceAll("ipfs://","https://ipfs.io/ipfs/");
    }
}

