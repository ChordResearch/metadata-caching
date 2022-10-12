package com.chord.nft.metadata.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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

    String s3BucketURL = "";

    @Value("${AWS_S3_BUCKET}")
    String bucketName;

    @Value("${AWS_S3_REGION}")
    String s3BucketRegion;

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
                .withRegion(Regions.fromName(s3BucketRegion))
                .build();
        s3BucketURL = "https://" + bucketName + ".s3." + s3BucketRegion + ".amazonaws.com";
    }


    public String upload(String filesToUpload) {
        String[] urlParts = filesToUpload.split("/");
        String outputFileName = urlParts[urlParts.length - 1];
        s3client.putObject(
                bucketName,
                "images/" + outputFileName,
                new File(filesToUpload)
        );
        return s3BucketURL + "/images/" + outputFileName;
    }

    public String download(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        String outputFileName = UUID.randomUUID().toString() + ".jpg"; //just assign some random file name
        String outputFilePath = "/tmp/";
        int retries = 10;
        try {
            InputStream in = url.openStream();
            while (in == null && retries > 0) {
                try {
                    in = url.openStream();
                }catch (Exception e){
                    // ignore if exception
                }
                retries--;
            }
            ReadableByteChannel rbc = Channels.newChannel(in);
            FileOutputStream fos = new FileOutputStream(outputFilePath + outputFileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return outputFilePath + outputFileName;
        } catch (Exception e) {
            return "";
        }
    }


    public JSONObject getMetadataFromTokenURI(String tokenURI) throws Exception {
        JSONObject metadata = new JSONObject();
        InputStream is = null;
        int retries = 10;
        try {
            System.out.println("token uri inside getMetadataFromTokenURI : " + tokenURI);
            while (is == null && retries > 0) {
                try {
                    is = new URL(tokenURI).openStream();
                }catch (Exception e){
                    // ignore if exception
                }
                retries--;
            }

            if (is != null) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                metadata = new JSONObject(jsonText);
            }
        } catch (Exception e) {
            System.out.println("Exception while fetching data from token uri : " + e.getMessage() + " , cause : " + e.getCause());
        } finally {
            if (is != null)
                is.close();
            return metadata;
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String downloadImageAndUploadToCDN(String fileURL) throws Exception {
        if(fileURL.isEmpty()) return "";

        String file = this.download(fileURL);
        System.out.println("downloaded image : " + file);
        String imageURL = this.upload(file);

        //clear file
        java.io.File fileToDelete = new File(file);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }

        return imageURL;
    }

    private String formatIpfsURL(String url) {
        return url.replaceAll("ipfs://", "https://ipfs.io/ipfs/");
    }
}

