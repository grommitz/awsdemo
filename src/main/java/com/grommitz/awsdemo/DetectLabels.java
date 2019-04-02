package com.grommitz.awsdemo;

import software.amazon.awssdk.services.rekognition.model.*;

import java.io.*;

/**
 * Simplest possible demo of the AWS Rekognition version 2 API
 */
public class DetectLabels {

    private static String[] urls = new String[]{
            "http://dtpmhvbsmffsz.cloudfront.net/posts/2016/01/02/56889322713fded89400d4e8/m_56889322713fded89400d4e9.jpg", // cartier handbag
            "http://picture-cdn.wheretoget.it/2ouij4-i.jpg", // michael kors handbag
            "http://images.wisegeek.com/gold-credit-card.jpg", // gold credit card on an angle
            "https://mlstaticquic-a.akamaihd.net/S_678719-MLU29685369102_032019-O.jpg", // camtasia studio 9
            "https://http2.mlstatic.com/S_757450-MPE29762995367_032019-O.jpg", // rosetta stone box
            "http://i.ebayimg.com/images/i/161381748444-0-1/s-l1000.jpg", // remanufactured ink cartiridges
            "https://brain-images-ssl.cdn.dixons.com/2/6/10071762/u_10071762.jpg"  // HP 62 ink cartridge
    };

    public static void main2(String[] args) {
        RekognitionApiClient client = new RekognitionApiClient();
        Image img = ImageUtil.getS3Image("nn-images", "barnett.png");
        client.detectLabels(img)
                .forEach(l -> {
                    System.out.println(l);
                    l.instances().forEach(i -> System.out.println("  "+i));
                });
    }

    public static void main(String[] args) throws IOException {
        RekognitionApiClient client = new RekognitionApiClient();
        Image img = ImageUtil.getImageFromURL(urls[0]);
        client.detectText(img)
                .forEach(l -> System.out.println(l));
        client.detectLabels(img)
                .forEach(l -> {
                    System.out.println(l);
                    l.instances().forEach(i -> System.out.println("  "+i));
                });
    }

}
