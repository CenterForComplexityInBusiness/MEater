package edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SetupProperty {

	String uiName() default "";

	String uiDescription() default "";

	// TODO probably we can just do this through pure reflection things
	SetupPropertyTypes propertyType();
}
