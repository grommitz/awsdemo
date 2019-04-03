package com.grommitz.awsdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.TextDetection;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ProductDetector {

	private static final Logger logger = LoggerFactory.getLogger(ProductDetector.class);
	static final String URL_FORMAT = "https://jmwym8qv12.execute-api.us-east-1.amazonaws.com/Public?img=%s&left=%s&top=%s&width=%s&height=%s";
	RekognitionApiClient client = new RekognitionApiClient();

	static class ProductDetectionResult {
		String concept;
		String text;
		double score;
		String url;

		public ProductDetectionResult(String concept, String text, double score, String url) {
			this.concept = concept;
			this.text = text;
			this.score = score;
			this.url = url;
		}

		@Override
		public String toString() {
			return String.format("[%s] concept=%s, textInImage=%s, score=%f, url=%s",
					this.getClass().getSimpleName(), concept, text, score, url);
		}
	}

	public Optional<ProductDetectionResult> detectProductWithLogo(String concept, Image img, String url) {

		logger.info("Analysing {} for '{}' and anything that might be a logo...", url, concept);

		List<Label> labels = client.detectLabels(img);

		logger.info("Found {} labels", labels.size());
		labels.forEach(l -> System.out.println(l)); //"label:"+l.name()+", confidence:"+l.confidence()));

		Label label = labels.stream()
				.filter(l -> l.name().toLowerCase().contains(concept.toLowerCase()) && l.confidence() > 75.0)
				.max(Comparator.comparing(Label::confidence))
				.orElse(null);

		logger.info("best label={}", label);

		return Optional.empty();
	}


	public Optional<ProductDetectionResult> detectProductWithText(String concept,
																  String textInImage,
																  Image img,
																  String url) throws IOException {

		logger.info("Analysing {} for '{}' and '{}'...", url, concept, textInImage);

		List<Label> labels = client.detectLabels(img);
		List<TextDetection> textDetections = client.detectText(img);

		logger.info("Found {} labels and {} text detections", labels.size(), textDetections.size());
		labels.forEach(l -> System.out.println(l));
		textDetections.forEach(t -> System.out.println(t));

		Label label = client.detectLabels(img).stream()
				.filter(l -> l.name().toLowerCase().contains(concept.toLowerCase()) && l.confidence() > 75.0)
				.max(Comparator.comparing(Label::confidence))
				.orElse(null);

		TextDetection text = client.detectText(img).stream()
				.filter(td -> td.detectedText().equalsIgnoreCase(textInImage) && td.confidence() > 0.75)
				.max(Comparator.comparing(TextDetection::confidence))
				.orElse(null);

		if (label != null && text != null) {
			logger.info("Found text '{}' and concept '{}' in the image!", textInImage, concept);

			BufferedImage bi = ImageUtil.toBufferedImage(img);

			int textLeft = (int) (text.geometry().boundingBox().left() * bi.getWidth());
			int textTop = (int) (text.geometry().boundingBox().top() * bi.getHeight());
			int textWidth = (int) (text.geometry().boundingBox().width() * bi.getWidth());
			int textHeight = (int) (text.geometry().boundingBox().height() * bi.getHeight());

			logger.info("Label has {} instances", label.instances().size());

			int labelLeft = 0;
			int labelTop = 0;
			int labelWidth = 0;
			int labelHeight = 0;

			if (!label.instances().isEmpty()) {
				labelLeft = (int) (label.instances().get(0).boundingBox().left() * bi.getWidth());
				labelTop = (int) (label.instances().get(0).boundingBox().top() * bi.getHeight());
				labelWidth = (int) (label.instances().get(0).boundingBox().width() * bi.getWidth());
				labelHeight = (int) (label.instances().get(0).boundingBox().height() * bi.getHeight());
			}

			String myurl = String.format(URL_FORMAT,
					URLEncoder.encode(url, "utf8"),
					""+textLeft+","+labelLeft,
					""+textTop+","+labelTop,
					""+textWidth+","+labelWidth,
					""+textHeight+","+labelHeight);

			return Optional.of(new ProductDetectionResult(concept, textInImage, 0.5, myurl));


		} else {
			logger.info("Not found.");
		}

		return Optional.empty();
	}

}
