package com.gmail.wysocki.maciej;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarLogoRecognizer {

	private static final Logger LOG = LoggerFactory.getLogger(CarLogoRecognizer.class);

	private static final String LEARNING_PATH = "/learningSamples/";
	
	private static final int OUTPUT_SIZE = 4;
	
	private static final double DEFAULT_MAX_ERROR = 0.1;
	
	@Autowired
	private ImageConverter converter;

	private NeuralNetwork<BackPropagation> ai;
	
	private double maxError;

	public CarLogoRecognizer() {
		ai = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, ImageConverter.SIZE * ImageConverter.SIZE, 10, OUTPUT_SIZE);
		maxError = DEFAULT_MAX_ERROR;
	}

	@PostConstruct
	protected void learn() throws VectorSizeMismatchException, IOException, InterruptedException {
		int i = 1;

		// Check network error on data used for learning and learn if error is too high
		while (!isTrained()) {
			LOG.info("Learning iteration {}", i++);
			if (i % 4 == 0) {
				ai.randomizeWeights(0.4, 0.6);
			}
			if (i > 20) {
				throw new RuntimeException("This network is untrainable.");
			}

			learnInNewThread(new LearningSet("merc01.jpg", 1, 0, 0, 0), new LearningSet("merc02.jpg", 1, 0, 0, 0),new LearningSet("bmw01.png", 0, 0, 0, 1),
					new LearningSet("mazda01.jpg", 0, 0, 1, 0));
			learnInNewThread(new LearningSet("ford01.jpg", 0, 1, 0, 0), new LearningSet("ford02.jpg", 0, 1, 0, 0),new LearningSet("bmw02.jpg", 0, 0, 0, 1),
					new LearningSet("mazda02.png", 0, 0, 1, 0));
			learnInNewThread(new LearningSet("merc03.jpg", 1, 0, 0, 0), new LearningSet("merc04.png", 1, 0, 0, 0),new LearningSet("bmw03.png", 0, 0, 0, 1),
					new LearningSet("mazda03.jpg", 0, 0, 1, 0));
			learnInNewThread(new LearningSet("ford03.png", 0, 1, 0, 0), new LearningSet("ford04.jpg", 0, 1, 0, 0),new LearningSet("bmw04.png", 0, 0, 0, 1),
					new LearningSet("mazda04.png", 0, 0, 1, 0));

			Thread.sleep(1000);
		}
	}

	private boolean isTrained() throws IOException {
		double error = getErrorForSets(new LearningSet("merc01.jpg", 1, 0, 0, 0), new LearningSet("ford01.jpg", 0, 1, 0, 0),
				new LearningSet("mazda01.jpg", 0, 0, 1, 0), new LearningSet("merc02.jpg", 1, 0, 0, 0), new LearningSet("ford02.jpg", 0, 1, 0, 0),
				new LearningSet("mazda02.png", 0, 0, 1, 0), new LearningSet("merc03.jpg", 1, 0, 0, 0), new LearningSet("ford03.png", 0, 1, 0, 0),
				new LearningSet("mazda03.jpg", 0, 0, 1, 0), new LearningSet("merc04.png", 1, 0, 0, 0), new LearningSet("ford04.jpg", 0, 1, 0, 0),
				new LearningSet("mazda04.png", 0, 0, 1, 0), new LearningSet("bmw01.png", 0, 0, 0, 1), new LearningSet("bmw02.jpg", 0, 0, 0, 1),
				new LearningSet("bmw03.png", 0, 0, 0, 1), new LearningSet("bmw04.png", 0, 0, 0, 1));
		LOG.debug("Last error is {}, max error is {}", error, maxError);
		return error < maxError;
	}

	private double getErrorForSets(LearningSet... sets) throws IOException {
		double lastError, error = 0;
		for (LearningSet set : sets) {
			lastError = getErrorForSet(set);
			if (lastError > error) {
				error = lastError;
			}
		}
		return error;
	}

	private double getErrorForSet(LearningSet set) throws IOException {
		double lastError, error = 0;
		BufferedImage image = ImageIO.read(getClass().getResourceAsStream(LEARNING_PATH + set.getImageName()));
		Answer answer = recognizeCarLogo(converter.convertToNormalized(image));
		for (int i = 0; i < OUTPUT_SIZE; i++) {
			lastError = Math.abs(set.getOutputValues()[i] - answer.getData()[i]);
			if (lastError > error) {
				error = lastError;
			}
		}
		return error;
	}

	private void learnInNewThread(LearningSet... set) throws IOException {
		DataSet trainingSet = new DataSet(ImageConverter.SIZE * ImageConverter.SIZE, OUTPUT_SIZE);
		for (int i = 0; i < set.length; ++i) {
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream(LEARNING_PATH + set[i].getImageName()));
			trainingSet.addRow(converter.convertToNormalized(image), set[i].getOutputValues());
		}
		ai.learnInNewThread(trainingSet);
	}

	public Answer recognizeCarLogo(double[] pixelsLineNormalized) {
		ai.setInput(pixelsLineNormalized);
		ai.calculate();
		return new Answer(ai.getOutput());
	}

	public void setConverter(ImageConverter converter) {
		this.converter = converter;
	}
	
	public void setMaxError(double maxError) {
		this.maxError = maxError;
	}

}
