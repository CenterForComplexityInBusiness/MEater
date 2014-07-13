package edu.umd.rhsmith.diads.meater.util.console;

public class BooleanPrompter implements ConsolePrompter<Boolean> {
	protected final String t;
	protected final String f;

	public BooleanPrompter(String t, String f) {
		this.t = t;
		this.f = f;
	}

	public Boolean accept(String input) {
		if (input.equalsIgnoreCase(t)) {
			return true;
		} else if (input.equalsIgnoreCase(f)) {
			return false;
		} else {
			throw new IllegalArgumentException(String.format(
					MSG_ERR_PICKONE_FMT, this.t, this.t));
		}
	}

	@Override
	public String getPromptText() {
		return String.format(MSG_PROMPT_BOOL_FMT, this.t, this.f);
	}

	private static final String MSG_ERR_PICKONE_FMT = "Enter either '%s' or '%s'.";
	private static final String MSG_PROMPT_BOOL_FMT = "[%s / %s]";
	private static final String MSG_PROMPT_BOOL_T = "t";
	private static final String MSG_PROMPT_BOOL_F = "f";
	private static final String MSG_PROMPT_BOOL_Y = "y";
	private static final String MSG_PROMPT_BOOL_N = "n";

	public static final ConsolePrompter<Boolean> PROMPT_TRUEFALSE = new BooleanPrompter(
			MSG_PROMPT_BOOL_T, MSG_PROMPT_BOOL_F);
	public static final ConsolePrompter<Boolean> PROMPT_YESNO = new BooleanPrompter(
			MSG_PROMPT_BOOL_Y, MSG_PROMPT_BOOL_N);
}