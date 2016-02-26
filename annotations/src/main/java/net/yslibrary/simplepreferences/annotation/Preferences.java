package net.yslibrary.simplepreferences.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yshrsmz on 2016/02/20.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Preferences {

  /**
   * preferences schema name.
   * if empty, default SharedPreferences is picked.
   *
   * @return
   */
  String value() default "";

  /**
   * whether or not use DefaultSharedPreferences
   *
   * @return
   */
  boolean useDefault() default false;
}
