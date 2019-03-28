package com.grommitz.awsdemo;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Simplest possible demo of the AWS Rekogntion version 2 API
 */
public class DetectLabels {

    public static void main(String[] args) throws IOException {

        RekognitionApiClient client = new RekognitionApiClient();

        Image img = getS3Image("nn-images", "barnett.png");
        client.detectLabels(img)
                .forEach(l -> System.out.println("label:"+l.name()+", confidence:"+l.confidence()));

        Image img2 = getImageFromURL(
                "http://i.ebayimg.com/images/i/161381748444-0-1/s-l1000.jpg");
                //https://brain-images-ssl.cdn.dixons.com/2/6/10071762/u_10071762.jpg");
        client.detectText(img2)
                .forEach(t -> System.out.println("text=" + t.detectedText() + ", confidence=" + t.confidence()));
    }

    private static Image getS3Image(String bucket, String name) {
        return Image.builder()
                .s3Object(S3Object.builder().bucket(bucket).name(name).build())
                .build();
    }

    private static Image getImageFromURL(String url_) throws IOException {
        URL url = new URL(url_);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "Firefox");

        try (InputStream inputStream = conn.getInputStream()) {
            int n = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
        return Image.builder()
                .bytes(SdkBytes.fromByteArray(output.toByteArray()))
                .build();
    }

}
