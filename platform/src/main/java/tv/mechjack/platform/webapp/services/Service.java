package tv.mechjack.platform.webapp.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Service {

  String uriPattern() default "";

  HttpMethod method() default HttpMethod.GET;

  String contentType() default "*/*";

}
