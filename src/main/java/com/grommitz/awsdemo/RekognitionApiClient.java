package com.grommitz.awsdemo;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;

public class RekognitionApiClient {

    private RekognitionClient rekognition;

    public RekognitionApiClient() {
        rekognition = RekognitionClient.builder()
                .region(Region.EU_WEST_1).build();
    }

    public List<Label> detectLabels(Image img) {
        DetectLabelsRequest request = DetectLabelsRequest.builder().image(img).build();
        DetectLabelsResponse response = rekognition.detectLabels(request);
        return response.labels();
    }

    public List<TextDetection> detectText(Image img) {
        DetectTextRequest textRequest = DetectTextRequest.builder().image(img).build();
        DetectTextResponse textResponse = rekognition.detectText(textRequest);
        return textResponse.textDetections();
    }

}
