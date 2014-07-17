package edu.umd.rhsmith.diads.meater.util.console;

public class DoublePrompter implements ConsolePrompter<Double> {
	@Override
	public Double accept(String input) {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public String getPromptText() {
		return null;
	}

	public static final ConsolePrompter<Double> PROMPT = new DoublePrompter();

}