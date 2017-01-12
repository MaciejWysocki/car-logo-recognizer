package com.gmail.wysocki.maciej;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainingTool {
	
	private static final Logger LOG = LoggerFactory.getLogger(TrainingTool.class);
	
	private static final String LEARNING_PATH = "/learningSamples/";
	
	protected static final String FULL_OUTPUT_WEIGHTS_PATH = "src/main/resources/";
	
	protected static final String OUTPUT_WEIGHTS_FILE_NAME = "weights.nn";
	
	private static final int OUTPUT_SIZE = 4;
	
	private static ImageConverter converter = new ImageConverter(false);

	public static void main(String[] args) throws URISyntaxException, IOException {
		NeuralNetwork<BackPropagation> nn = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, ImageConverter.SIZE * ImageConverter.SIZE, 10, OUTPUT_SIZE);
		
		// load list of learning samples
		URL resource = TrainingTool.class.getResource(LEARNING_PATH);
		String[] samples = new File(resource.toURI()).list();
		LOG.debug(Arrays.toString(samples));

		// traing
		DataSet trainingSet = new DataSet(ImageConverter.SIZE * ImageConverter.SIZE, OUTPUT_SIZE);
		for (String sample : samples) {
			LOG.debug(sample);
			BufferedImage image = loadImageFromResource(sample);
			trainingSet.addRow(converter.convertToNormalized(image), readOutputFromSampleName(sample));			
		}
		// special edge case, empty image
		trainingSet.addRow(converter.convertToNormalized(ImageIO.read(TrainingTool.class.getResourceAsStream("/empty.png"))), new double[] {0, 0, 0, 0});
		
		for(int i = 0; i < 100; ++i) {
			nn.learn(trainingSet);
			double error = getErrorForSamples(nn, samples);
			LOG.debug("Err0r is {}", error);
		}
		
		// save network weights
		nn.save(FULL_OUTPUT_WEIGHTS_PATH + OUTPUT_WEIGHTS_FILE_NAME);
	}

	private static BufferedImage loadImageFromResource(String sample) throws IOException {
		return ImageIO.read(TrainingTool.class.getResourceAsStream(LEARNING_PATH + sample));
	}

	private static double getErrorForSamples(NeuralNetwork<BackPropagation> nn, String[] samples) throws IOException {
		double lastError, error = 0;
		for (String sample : samples) {
			nn.setInput(converter.convertToNormalized(loadImageFromResource(sample)));
			nn.calculate();
			double[] answer = nn.getOutput();
			for (int i = 0; i < OUTPUT_SIZE; i++) {
				lastError = Math.abs(readOutputFromSampleName(sample)[i] - answer[i]);
				if (lastError > error) {
					error = lastError;
				}
			}
		}
		return error;
	}

	private static double[] readOutputFromSampleName(String sample) {
		double[] output = new double[OUTPUT_SIZE];
		for (int i = 0; i < output.length; ++i) {
			output[i] = 0;
		}
		output[Integer.valueOf(sample.split("-")[0])] = 1;
		return output;
	}

}
