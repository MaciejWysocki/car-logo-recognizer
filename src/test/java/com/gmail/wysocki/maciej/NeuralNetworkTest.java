package com.gmail.wysocki.maciej;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NeuralNetworkTest {

	private NeuralNetwork ai;

	@Before
	public void setUp() {
		ai = new NeuralNetwork();
	}

	@Test
	public void shouldRecognizeMazda() {
		BufferedImage image = extracted("/mazda.jpg");
		int height = image.getHeight();
		int width = image.getWidth();
		int[][] mazdaLogo = new int[width][height];
		System.out.println("width: " + width + ", height: " + height);

		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				mazdaLogo[i][j] = image.getRGB(i, j);
			}
		}

		Answer answer = ai.recognizeCarLogo(mazdaLogo);
		Assert.assertEquals("Mazda", answer.getAnswer());
	}

	private BufferedImage extracted(String fileName) {
		try {
			return ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
