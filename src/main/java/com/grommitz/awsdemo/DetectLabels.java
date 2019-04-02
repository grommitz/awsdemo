package com.grommitz.awsdemo;

import software.amazon.awssdk.services.rekognition.model.*;

import java.io.*;

/**
 * Simplest possible demo of the AWS Rekognition version 2 API
 */
public class DetectLabels {

    public static void main(String[] args) throws IOException {

        RekognitionApiClient client = new RekognitionApiClient();

//        Image img = getS3Image("nn-images", "barnett.png");
//        client.detectLabels(img)
//                .forEach(l -> System.out.println("label:"+l.name()+", confidence:"+l.confidence()));

        Image img2 = ImageUtil.getImageFromURL("http://images.wisegeek.com/gold-credit-card.jpg");
                //"https://mlstaticquic-a.akamaihd.net/S_678719-MLU29685369102_032019-O.jpg");
                //"https://http2.mlstatic.com/S_757450-MPE29762995367_032019-O.jpg");
                //"http://i.ebayimg.com/images/i/161381748444-0-1/s-l1000.jpg");
                //https://brain-images-ssl.cdn.dixons.com/2/6/10071762/u_10071762.jpg");
        client.detectText(img2)
                .forEach(t -> System.out.println(t));
//                .forEach(t -> System.out.println("text=" + t.detectedText() + ", confidence=" + t.confidence()));
    }



}
