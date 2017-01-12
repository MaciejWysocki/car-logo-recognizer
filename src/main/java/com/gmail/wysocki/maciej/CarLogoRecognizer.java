package com.gmail.wysocki.maciej;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.learning.BackPropagation;
import org.springframework.stereotype.Component;

@Component
public class CarLogoRecognizer {

	private NeuralNetwork<BackPropagation> ai;

	@SuppressWarnings("unchecked")
	public CarLogoRecognizer() {
		ai = NeuralNetwork.createFromFile(TrainingTool.FULL_OUTPUT_WEIGHTS_PATH + TrainingTool.OUTPUT_WEIGHTS_FILE_NAME);
	}

	public Answer recognizeCarLogo(double[] pixelsLineNormalized) {
		ai.setInput(pixelsLineNormalized);
		ai.calculate();
		return new Answer(ai.getOutput());
	}

}