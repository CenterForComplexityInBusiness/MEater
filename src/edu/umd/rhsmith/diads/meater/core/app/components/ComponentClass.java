package edu.umd.rhsmith.diads.meater.core.app.components;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentClass {
	String foo();
}
