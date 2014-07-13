package edu.umd.rhsmith.diads.meater.util.console;

public class IntPrompter implements ConsolePrompter<Integer> {
	public Integer accept(String input) {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public String getPromptText() {
		return null;
	}

	public static final ConsolePrompter<Integer> PROMPT = new IntPrompter();

}