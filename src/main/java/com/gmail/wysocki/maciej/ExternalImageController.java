package com.gmail.wysocki.maciej;

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
	private NeuralNetwork ai;

    @RequestMapping(value = "recognize/{pictureUrl}", method = RequestMethod.GET)
    public Answer recognize(@PathVariable String pictureUrl) {
    	// validate image
    	validator.validate(pictureUrl);
    	
    	// download image
    	byte[] image = downloader.download(pictureUrl);
    	
    	// change to bitmap
    	int[][] bitmap = converter.convertToBitmap(image);
    	
    	// invoke neural network and return an answer
    	return ai.recognizeCarLogo(bitmap);
    }
}
