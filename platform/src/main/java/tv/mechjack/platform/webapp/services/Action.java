package tv.mechjack.platform.webapp.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {

  enum Type {
    LIST, CREATE, SHOW, UPDATE, DESTROY
  }

  Type type();

  String contentType() default "*/*";

}
