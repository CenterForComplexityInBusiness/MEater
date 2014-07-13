package edu.umd.rhsmith.diads.meater.util.console;

import static edu.umd.rhsmith.diads.meater.util.Util.indentLines;
import static java.lang.String.format;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Scanner;

public class Console {
	// i/o
	private final PrintStream out;
	private final Scanner in;

	// indentation state
	private int totalIndent;
	private LinkedList<Integer> indents;
	private String indentSequence;

	public Console(InputStream in, OutputStream out, String indentSequence) {
		this.totalIndent = 0;
		this.indents = new LinkedList<Integer>();
		this.indentSequence = indentSequence;

		this.out = new PrintStream(out);
		this.in = new Scanner(in);
	}

	public void pushIndent(int level) {
		if (level < 0 || level >= DIVIDERS.length) {
			throw new IllegalArgumentException();
		}

		totalIndent += level;
		indents.addLast(level);
	}

	public void popIndent() {
		totalIndent -= indents.removeLast();
	}

	public int getIndentLevel() {
		return this.indents.size();
	}

	public void divide(int level) {
		if (level < 0) {
			throw new IllegalArgumentException();
		}

		say(DIVIDERS[Math.min(level, DIVIDERS.length - 1)]);
	}

	public void sayNoNewline(String message, Object... args) {
		out.print(indentLines(format(message, args), totalIndent,
				indentSequence));
	}

	public void say(String message, Object... args) {
		out.println(indentLines(format(message, args), totalIndent,
				indentSequence));
	}

	public void warn(String message, Object... args) {
		out.println(indentLines(format(FMT_MSG_WARN, format(message, args)),
				totalIndent, indentSequence));
	}

	public void error(String message, Object... args) {
		out.println(indentLines(format(FMT_MSG_ERROR, format(message, args)),
				totalIndent, indentSequence));
	}

	public void sayNoNewline(String message) {
		out.print(indentLines(message, totalIndent, indentSequence));
	}

	public void say(String message) {
		out.println(indentLines(message, totalIndent, indentSequence));
	}

	public void warn(String message) {
		out.println(indentLines(format(FMT_MSG_WARN, message), totalIndent,
				indentSequence));
	}

	public void error(String message) {
		out.println(indentLines(format(FMT_MSG_ERROR, message), totalIndent,
				indentSequence));
	}

	/*
	 * --------------------------------
	 * Prompt user for things
	 * --------------------------------
	 */

	public String prompt(boolean breakOnEmpty) {
		while (true) {
			sayNoNewline(MSG_PROMPT);
			String input = in.nextLine();
			if (breakOnEmpty && input.isEmpty()) {
				break;
			} else {
				return input;
			}
		}

		return null;
	}

	public <E> E prompt(ConsolePrompter<E> prompt, boolean breakOnEmpty) {
		while (true) {
			if (prompt.getPromptText() != null) {
				sayNoNewline(MSG_PROMPT_FMT, prompt.getPromptText());
			} else {
				sayNoNewline(MSG_PROMPT);
			}
			try {
				String input = in.nextLine();
				if (breakOnEmpty && input.isEmpty()) {
					break;
				} else {
					return (E) prompt.accept(input);
				}
			} catch (IllegalArgumentException e) {
				error(e.getMessage());
			}
		}

		return null;
	}

	/*
	 * --------------------------------
	 * Some general-purpose console prompts
	 * --------------------------------
	 */

	/*
	 * --------------------------------
	 * Some constants for output
	 * --------------------------------
	 */

	// prompts
	private static final String MSG_PROMPT = "> ";
	private static final String MSG_PROMPT_FMT = "%s > ";

	// screen dividers
	private static final String[] DIVIDERS = new String[6];
	static {
		DIVIDERS[0] = "---";
		DIVIDERS[1] = "-----";
		for (int x = 2; x < DIVIDERS.length; x++) {
			DIVIDERS[x] = DIVIDERS[x - 1] + DIVIDERS[x - 1];
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	// prefixes for warning, error
	private static final String FMT_MSG_ERROR = "###Error: %s";
	private static final String FMT_MSG_WARN = ">>>Warning: %s";
}
