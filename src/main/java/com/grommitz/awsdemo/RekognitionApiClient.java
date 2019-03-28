package com.grommitz.awsdemo;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.List;

public class RekognitionApiClient {

    RekognitionClient rekognition;

    public RekognitionApiClient() {
        rekognition = RekognitionClient.builder()
                .region(Region.EU_WEST_1).build();
    }

    public List<Label> getLabels(Image img) {

        DetectLabelsRequest request = DetectLabelsRequest.builder().image(img).build();

        DetectLabelsResponse response = rekognition.detectLabels(request);

        response.labels()
                .forEach(l -> System.out.println("label:"+l.name()+", confidence:"+l.confidence()));

        return response.labels();
    }

    public List<TextDetection> getText(Image img) {

        DetectTextRequest textRequest = DetectTextRequest.builder().image(img).build();

        DetectTextResponse textResponse = rekognition.detectText(textRequest);

        textResponse.textDetections()
                .forEach(l -> System.out.println("detection:"+ l.detectedText() +", confidence:"+l.confidence()));

        return textResponse.textDetections();

    }


}
