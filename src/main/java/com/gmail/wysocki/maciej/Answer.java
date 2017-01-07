package com.gmail.wysocki.maciej;

import java.util.Arrays;

public class Answer {

	private double[] data;

	public Answer(double[] output) {
		data = output;
	}
	
	@Override
	public String toString() {
		return "Answer: " + Arrays.toString(data);
	}
	
	public double[] getData() {
		return data;
	}

}
