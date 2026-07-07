package io.github.hison.api.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

/**
 * Marks a Spring service bean as exposed through api-link (cmd based).
 *
 * <p>This annotation is meta-annotated with {@link Service}, so a class annotated with
 * {@code @ApiLinkService} is registered as a Spring bean <b>and</b> exposed to api-link with a
 * single annotation — no separate {@code @Service} is needed.</p>
 *
 * <p>api-link only invokes methods on beans marked with this annotation (or the deprecated
 * {@link HisonService}); beans without it are rejected with {@code APIERROR0007}. This narrows the
 * call surface to explicitly opted-in services.</p>
 *
 * <pre>
 *   &#64;ApiLinkService
 *   public class MemberService {
 *       public DataWrapper getMember(DataWrapper req) { ... }
 *   }
 *   // client: cmd = "MemberService.getMember"
 * </pre>
 *
 * @author Hani son
 * @version 2.0.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Service
public @interface ApiLinkService {}
