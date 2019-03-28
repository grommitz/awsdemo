package com.grommitz.awsdemo;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

/**
 * Simplest possible demo of the AWS Rekogntion version 2 API
 */
public class DetectLabels {

    public static void main(String[] args) {

        RekognitionClient rekognition = RekognitionClient.builder()
                .region(Region.EU_WEST_1).build();

        Image img = Image.builder()
                .s3Object(S3Object.builder().bucket("nn-images").name("barnett.png").build())
                .build();

        DetectLabelsRequest request = DetectLabelsRequest.builder().image(img).build();

        DetectLabelsResponse response = rekognition.detectLabels(request);

        response.labels()
                .forEach(l -> System.out.println("label:"+l.name()+", confidence:"+l.confidence()));

    }

}
