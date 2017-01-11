package com.gmail.wysocki.maciej;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class Answer {

	private double[] data;

	public Answer(double[] output) {
		// cutting precision
		data = DoubleStream.of(output).map(o -> ((int)(1000 * o)) / 1000d).toArray();
	}
	
	@Override
	public String toString() {
		return "Answer: " + Arrays.toString(data);
	}
	
	public double[] getData() {
		return data;
	}

}
