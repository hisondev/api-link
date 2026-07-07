package io.github.hison.api.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for exposing service beans through hison api-link (cmd based).
 *
 * @deprecated Use {@link ApiLinkService} instead. Unlike this marker, {@code @ApiLinkService} is
 * meta-annotated with {@code @Service}, so a single annotation both registers the Spring bean and
 * exposes it to api-link. This annotation is still recognized for backward compatibility, but a class
 * annotated only with {@code @HisonService} must also be a Spring bean (e.g. via {@code @Service}).
 *
 * @author Hani son
 * @version 2.0.2
 */
@Deprecated
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HisonService {}