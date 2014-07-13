package edu.umd.rhsmith.diads.meater.util.console;

public class FloatPrompter implements ConsolePrompter<Float> {
	public Float accept(String input) {
		try {
			return Float.parseFloat(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public String getPromptText() {
		return null;
	}

	public static final ConsolePrompter<Float> PROMPT = new FloatPrompter();

}