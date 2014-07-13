package edu.umd.rhsmith.diads.meater.util.console;

public interface ConsolePrompter<E> {

	public String getPromptText();

	public E accept(String input) throws IllegalArgumentException;
}
