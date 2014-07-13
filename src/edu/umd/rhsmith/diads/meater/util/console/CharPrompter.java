package edu.umd.rhsmith.diads.meater.util.console;

public class CharPrompter implements ConsolePrompter<Character> {

	public Character accept(String input) {
		if (input.length() == 1) {
			return input.charAt(0);
		} else {
			throw new IllegalArgumentException(MSG_ERR_ONECHAR);
		}
	}

	@Override
	public String getPromptText() {
		return null;
	}

	private static final String MSG_ERR_ONECHAR = "Enter a single character.";

	public static final ConsolePrompter<Character> PROMPT = new CharPrompter();

}