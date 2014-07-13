package edu.umd.rhsmith.diads.meater.util.console;

public class LongPrompter implements ConsolePrompter<Long> {
	public Long accept(String input) {
		try {
			return Long.parseLong(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public String getPromptText() {
		return null;
	}

	public static final ConsolePrompter<Long> PROMPT = new LongPrompter();

}