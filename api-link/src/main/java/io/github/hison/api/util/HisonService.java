package io.github.hison.api.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Marker annotation for exposing service beans through hison api-link (cmd based).
 * 
 * @author Hani son
 * @version 2.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HisonService {}