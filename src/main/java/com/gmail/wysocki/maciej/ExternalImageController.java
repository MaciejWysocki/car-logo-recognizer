package com.gmail.wysocki.maciej;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ExternalImageController {
	
	@Autowired
	private ImageConverter converter;
	@Autowired
	private CarLogoRecognizer recognizer;

    @RequestMapping(value = "recognize", method = RequestMethod.POST)
    public Answer recognize(@RequestBody String pictureBase64encoded) throws IOException {
    	// decode the image
    	byte[] decoded = Base64.getDecoder().decode(pictureBase64encoded.split(",")[1]);
		Image image = ImageIO.read(new ByteArrayInputStream(decoded));
	
    	// change to 0-1 values where each number is one pixel
    	double[] pixelsLineNormalized = converter.convertToNormalized(image);
    	
    	return recognizer.recognizeCarLogo(pixelsLineNormalized);
    }
}
