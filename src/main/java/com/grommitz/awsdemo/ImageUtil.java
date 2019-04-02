package com.grommitz.awsdemo;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageUtil {

    public static Image getImageFromURL(String url_) throws IOException {
        URL url = new URL(url_);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "Firefox");

        try (InputStream inputStream = conn.getInputStream()) {
            int n;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
        return Image.builder()
                .bytes(SdkBytes.fromByteArray(output.toByteArray()))
                .build();
    }

    public static Image getS3Image(String bucket, String name) {
        return Image.builder()
                .s3Object(S3Object.builder().bucket(bucket).name(name).build())
                .build();
    }

    public static BufferedImage toBufferedImage(Image img) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(img.bytes().asByteArray()));
    }

}
