package edu.umd.rhsmith.diads.meater.util.console;

public class StringPrompter implements ConsolePrompter<String> {
	public String accept(String input) {
		return input;
	}

	@Override
	public String getPromptText() {
		return null;
	}

	public static final ConsolePrompter<String> PROMPT = new StringPrompter();

}