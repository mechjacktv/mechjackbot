package tv.mechjack.platform.webapp.api.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tv.mechjack.platform.webapp.api.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestHandler {

  String uriPattern() default "";

  HttpMethod method() default HttpMethod.GET;

  String contentType() default "*/*";

}
