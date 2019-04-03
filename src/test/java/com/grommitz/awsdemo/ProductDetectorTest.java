package com.grommitz.awsdemo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.rekognition.model.Image;

import java.io.IOException;
import java.util.Optional;

import static com.grommitz.awsdemo.ImageUtil.getImageFromURL;
import static com.grommitz.awsdemo.TestUrls.CARTIER_BAGS;
import static com.grommitz.awsdemo.TestUrls.GUCCI_BAGS;
import static org.junit.Assert.*;

public class ProductDetectorTest {

	private static final Logger logger = LoggerFactory.getLogger(ProductDetectorTest.class);

	@Test
	public void testProductWithText() throws IOException {
		String url = CARTIER_BAGS[4];
		Image img = getImageFromURL(url);
		Optional<ProductDetector.ProductDetectionResult> result = new ProductDetector()
				.detectProductWithText("handbag", "cartier", img, url);

		if (result.isPresent())
			logger.info(""+result.get());
	}

	@Test
	public void testProductWithGucciText() throws IOException {
		String url = GUCCI_BAGS[4];
		Image img = getImageFromURL(url);
		Optional<ProductDetector.ProductDetectionResult> result = new ProductDetector()
				.detectProductWithText("handbag", "Gucci", img, url);

		if (result.isPresent())
			logger.info(""+result.get());
	}

	@Test
	public void testProductWithLogo() throws IOException {
		String url = GUCCI_BAGS[2];
		Image img = getImageFromURL(url);
		Optional<ProductDetector.ProductDetectionResult> result = new ProductDetector()
				.detectProductWithLogo("handbag", img, url);

		if (result.isPresent())
			logger.info(""+result.get());
	}



}