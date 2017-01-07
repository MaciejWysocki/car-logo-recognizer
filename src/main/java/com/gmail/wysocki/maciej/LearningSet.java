package com.gmail.wysocki.maciej;

public class LearningSet {

	private String imageName;

	private double[] outputValues;
	
	public LearningSet(String imageName, double... outputValues) {
		this.imageName = imageName;
		this.outputValues = outputValues;
	}
	
	public String getImageName() {
		return imageName;
	}

	public double[] getOutputValues() {
		return outputValues;
	}
}
