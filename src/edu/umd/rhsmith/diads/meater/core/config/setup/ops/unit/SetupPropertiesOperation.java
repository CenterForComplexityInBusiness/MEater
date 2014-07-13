package edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit;

import java.lang.reflect.Field;

import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;
import edu.umd.rhsmith.diads.meater.util.console.BytePrompter;
import edu.umd.rhsmith.diads.meater.util.console.CharPrompter;
import edu.umd.rhsmith.diads.meater.util.console.DoublePrompter;
import edu.umd.rhsmith.diads.meater.util.console.EmptyStringPrompter;
import edu.umd.rhsmith.diads.meater.util.console.FloatPrompter;
import edu.umd.rhsmith.diads.meater.util.console.IntPrompter;
import edu.umd.rhsmith.diads.meater.util.console.LongPrompter;
import edu.umd.rhsmith.diads.meater.util.console.ShortPrompter;

public class SetupPropertiesOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "setup-props";
	public static final String OP_UINAME = "Set up current selection's properties";

	private final ConfigUnit owner;

	public SetupPropertiesOperation(String uiName, String shortName,
			ConfigUnit owner) {
		super(uiName, shortName);

		this.owner = owner;
	}

	public SetupPropertiesOperation(ConfigUnit owner) {
		this(OP_UINAME, OP_SHORTNAME, owner);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().pushIndent(1);
		setupFields(setup);
		setup.getConsole().popIndent();
	}

	private void setupFields(MEaterSetupConsole setup) {
		boolean hadFields = false;

		Class<?> c = owner.getClass();

		// set up all setup-property-annotated fields walking up the class
		// heirarchy
		while (c != null) {
			Field[] fields = c.getDeclaredFields();
			for (Field f : fields) {
				SetupProperty sp = f.getAnnotation(SetupProperty.class);
				if (sp != null) {
					hadFields = true;
					try {
						f.setAccessible(true);
						f.set(this.owner, this.promptValue(setup, sp, f
								.get(this.owner)));
					} catch (IllegalArgumentException e) {
						setup.getConsole().error("%s", e.getMessage());
					} catch (IllegalAccessException e) {
						setup.getConsole().error("%s", e.getMessage());
					} catch (SecurityException e) {
						setup.getConsole().error("%s", e.getMessage());
					}
				}
			}
			c = c.getSuperclass();
		}

		if (!hadFields) {
			setup.getConsole().say(MSG_NO_FIELDS_FMT, this.owner.getUiName());
		}
	}

	private Object promptValue(MEaterSetupConsole setup, SetupProperty sp,
			Object currValue) {
		setup.getConsole().say(MSG_INFO_FMT, sp.uiName(),
				sp.propertyType().getUiNameName());
		if (sp.uiDescription() != null && !sp.uiDescription().isEmpty()) {
			setup.getConsole().say(MSG_HELPTEXT_FMT, sp.uiDescription());
		}
		setup.getConsole().say(MSG_CURRVAL_FMT, currValue);
		Object value;
		switch (sp.propertyType()) {
		case BOOLEAN:
			value = setup.getConsole().prompt(BooleanPrompter.PROMPT_TRUEFALSE,
					true);
			break;
		case BYTE:
			value = setup.getConsole().prompt(BytePrompter.PROMPT, true);
			break;
		case CHAR:
			value = setup.getConsole().prompt(CharPrompter.PROMPT, true);
			break;
		case SHORT:
			value = setup.getConsole().prompt(ShortPrompter.PROMPT, true);
			break;
		case INT:
			value = setup.getConsole().prompt(IntPrompter.PROMPT, true);
			break;
		case LONG:
			value = setup.getConsole().prompt(LongPrompter.PROMPT, true);
			break;
		case FLOAT:
			value = setup.getConsole().prompt(FloatPrompter.PROMPT, true);
			break;
		case DOUBLE:
			value = setup.getConsole().prompt(DoublePrompter.PROMPT, true);
			break;
		case STRING:
			value = setup.getConsole().prompt(EmptyStringPrompter.PROMPT, true);
			break;
		default:
			setup.getConsole().warn(MSG_WARN_UNKNOWN_ANNOTATION);
			value = null;
		}

		// if we got nothing, default to the current value
		if (value == null) {
			value = currValue;
		}

		return value;
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_CURRVAL_FMT = "(current value: '%s' -- enter empty line to retain)";
	private static final String MSG_HELPTEXT_FMT = "-- %s";
	private static final String MSG_INFO_FMT = "Property: %s (%s)";
	private static final String MSG_WARN_UNKNOWN_ANNOTATION = "Property was not annotated with a known property type -- using current value (you should fix this)";
	private static final String MSG_NO_FIELDS_FMT = "Component %s had no fields to set up";

}
