package edu.umd.rhsmith.diads.meater.util.console;

public class EmptyStringPrompter extends StringPrompter {

	@Override
	public String accept(String input) {
		if (input.equals(SPECIAL_EMPTY)) {
			return "";
		} else {
			return input;
		}
	}

	@Override
	public String getPromptText() {
		return MSG_PROMPT;
	}

	private static final String MSG_PROMPT = "[<empty> for empty string]";
	private static final Object SPECIAL_EMPTY = "<empty>";

	public static final ConsolePrompter<String> PROMPT = new EmptyStringPrompter();
}
