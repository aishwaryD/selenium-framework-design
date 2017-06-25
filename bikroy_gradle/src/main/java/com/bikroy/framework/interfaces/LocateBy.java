package com.bikroy.framework.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
// Can be used only on Fields.
public @interface LocateBy {
	public String xpath() default "";

	public String name() default "";

	public String css() default "";

	public String id() default "";
}