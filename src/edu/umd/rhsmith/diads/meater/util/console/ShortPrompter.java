package edu.umd.rhsmith.diads.meater.util.console;

public class ShortPrompter implements ConsolePrompter<Short> {
	@Override
	public Short accept(String input) {
		try {
			return Short.parseShort(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public String getPromptText() {
		return null;
	}

	public static final ConsolePrompter<Short> PROMPT = new ShortPrompter();

}