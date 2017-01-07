package com.gmail.wysocki.maciej;

import java.awt.Image;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalImageController {
	
	@Autowired
	private UrlValidator validator;
	@Autowired
	private ImageDownloader downloader;
	@Autowired
	private ImageConverter converter;
	@Autowired
	private CarLogoRecognizer recognizer;

    @RequestMapping(value = "recognize/{pictureUrl}", method = RequestMethod.GET)
    public Answer recognize(@PathVariable String pictureUrl) throws IOException {
    	// validate image
    	validator.validate(pictureUrl);
    	
    	// download image
    	Image image = downloader.download(pictureUrl);
    	
    	// change to bitmap
    	double[] pixelsLineNormalized = converter.convertToNormalized(image);
    	
    	return recognizer.recognizeCarLogo(pixelsLineNormalized);
    }
}
