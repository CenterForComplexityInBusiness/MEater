package edu.umd.rhsmith.diads.meater.util.console;

public class BytePrompter implements ConsolePrompter<Byte> {
	@Override
	public Byte accept(String input) {
		try {
			return Byte.parseByte(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public String getPromptText() {
		return null;
	}

	public static final ConsolePrompter<Byte> PROMPT = new BytePrompter();

}