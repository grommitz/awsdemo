package com.grommitz.awsdemo;

import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.TextDetection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Identify credit card number in an image & draw bounding box
 */
public class CreditCardNumberFinder {

    public static final String LAMBDA_URL = "https://l4xyidxa0b.execute-api.us-east-1.amazonaws.com/Public?";
    public static final String ALT_URL    = "https://jmwym8qv12.execute-api.us-east-1.amazonaws.com/Public?"; //img=https%3A%2F%2Fs3.amazonaws.com%2Fmturk-solutions%2Ftutorials%2Fimages%2Fmachines.jpg&left=140,700&top=210,210&width=300,310&height=530,550

    public static void main(String[] args) throws IOException {
        new CreditCardNumberFinder().run("http://images.wisegeek.com/gold-credit-card.jpg");
    }

    private void run(String imgUrl) throws IOException {
        RekognitionApiClient client = new RekognitionApiClient();
        Image img2 = ImageUtil.getImageFromURL(imgUrl);
        List<TextDetection> ccs = client.detectText(img2)
                .stream()
                .filter(this::isCCNumber)
                .collect(Collectors.toList());

        System.out.println("Found " + ccs.size() + " credit card numbers");
        ccs.forEach(t -> System.out.println("CREDIT CARD NUMBER: " + t.detectedText()));
        if (ccs.isEmpty()) {
            return;
        }

        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(img2.bytes().asByteArray()));

        // next - draw bounding box & display it
        for (TextDetection ccNum : ccs) {
            System.out.println(ccNum);
            String bbUrl = String.format("%simg=%s&left=%d&top=%d&width=%d&height=%d",
                    LAMBDA_URL,
                    URLEncoder.encode(imgUrl, "utf8"),
                    (int)(ccNum.geometry().boundingBox().left() * bi.getWidth()),
                    (int)(ccNum.geometry().boundingBox().top() * bi.getHeight()),
                    (int)(ccNum.geometry().boundingBox().width() * bi.getWidth()),
                    (int)(ccNum.geometry().boundingBox().height() * bi.getHeight()));
            System.out.println(bbUrl);

            int left = (int)(bi.getWidth() *
                    ccNum.geometry().polygon().stream().map(pt -> pt.x())
                            .min(Comparator.naturalOrder())
                            .orElse(0.0f));
            int top = (int)(bi.getHeight() *
                    ccNum.geometry().polygon().stream().map(pt -> pt.y())
                            .min(Comparator.naturalOrder())
                            .orElse(0.0f));
            int width = (int)(bi.getWidth() *
                    ccNum.geometry().polygon().stream().map(pt -> pt.x())
                            .max(Comparator.naturalOrder())
                            .orElse(0.0f)) - left;
            int height = (int)(bi.getHeight() *
                    ccNum.geometry().polygon().stream().map(pt -> pt.y())
                            .max(Comparator.naturalOrder())
                            .orElse(0.0f)) - top;

            String bbUrl2 = String.format("%simg=%s&left=%d&top=%d&width=%d&height=%d",
                    LAMBDA_URL,
                    URLEncoder.encode(imgUrl, "utf8"), left, top, width, height);

            System.out.println(bbUrl2);
        }
    }

    private boolean isCCNumber(TextDetection textDetection) {
        return textDetection.detectedText()
                .replaceAll("[ ]+", "")
                .matches("[0-9]{16}");
    }


}
