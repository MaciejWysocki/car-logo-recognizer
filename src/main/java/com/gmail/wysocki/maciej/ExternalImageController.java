package com.gmail.wysocki.maciej;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ExternalImageController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExternalImageController.class);
	
	@Autowired
	private UrlValidator validator;
	@Autowired
	private ImageDownloader downloader;
	@Autowired
	private ImageConverter converter;
	@Autowired
	private CarLogoRecognizer recognizer;

    @RequestMapping(value = "recognize", method = RequestMethod.POST)
    public Answer recognize(@RequestBody String pictureBase64encoded) throws IOException {
    	// validate image
//    	validator.validate(pictureUrl);

    	// download image
//    	Image image = downloader.download(pictureUrl);
//    	LOG.debug(pictureBase64encoded);
    	byte[] decoded = Base64.getDecoder().decode(pictureBase64encoded.split(",")[1]);
//    	Image image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
		Image image = ImageIO.read(new ByteArrayInputStream(decoded));

    	
    	// change to bitmap
    	double[] pixelsLineNormalized = converter.convertToNormalized(image);
    	
    	return recognizer.recognizeCarLogo(pixelsLineNormalized);
    }
}
