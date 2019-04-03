package com.grommitz.awsdemo;

import software.amazon.awssdk.services.rekognition.model.*;

import java.io.*;

/**
 * Simplest possible demo of the AWS Rekognition version 2 API
 */
public class DetectLabels {

    public static void main(String[] args) throws IOException {
        labelDemoFromS3();
        textDetectionDemoFromURL();
    }

    private static void labelDemoFromS3() {
        RekognitionApiClient client = new RekognitionApiClient();
        Image img = ImageUtil.getS3Image("nn-images", "barnett.png");
        client.detectLabels(img)
                .forEach(l -> System.out.println(l));
    }

    public static void textDetectionDemoFromURL() throws IOException {
        RekognitionApiClient client = new RekognitionApiClient();
        Image img = ImageUtil.getImageFromURL("http://picture-cdn.wheretoget.it/2ouij4-i.jpg");
        client.detectText(img)
                .forEach(l -> System.out.println(l));
    }


}
