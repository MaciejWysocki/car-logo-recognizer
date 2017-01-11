package com.gmail.wysocki.maciej;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class ImageConverter {

	public static final int SIZE = 16;

	public double[] convertToNormalized(Image image) throws IOException {
		double[] converted = new double[SIZE * SIZE];

		BufferedImage shrinked = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = shrinked.createGraphics();
		g.drawImage(image, 0, 0, SIZE, SIZE, new Color(255, 255, 255), null);
		g.dispose();

		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				Color c = new Color(shrinked.getRGB(column, row));
				converted[row * SIZE + column] = (c.getRed() + c.getGreen() + c.getBlue()) / 765d;
			}
		}

//		dumpConverted(converted);
		return converted;
	}

	private void dumpConverted(double[] converted) throws IOException {
		byte[] imageInByte = new byte[converted.length * 4];
		for (int i = 0; i < converted.length; ++i) {
			imageInByte[4 * i] = (byte) 255;
			imageInByte[4 * i + 1] = (byte) (255 * converted[i]);
			imageInByte[4 * i + 2] = (byte) (255 * converted[i]);
			imageInByte[4 * i + 3] = (byte) (255 * converted[i]);
		}

		BufferedImage image2 = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_4BYTE_ABGR);
		byte[] data = ((DataBufferByte) image2.getRaster().getDataBuffer()).getData();
		System.arraycopy(imageInByte, 0, data, 0, imageInByte.length);

		ImageIO.write(image2, "png", new File("/tmp/image " + System.currentTimeMillis()));
	}

}
