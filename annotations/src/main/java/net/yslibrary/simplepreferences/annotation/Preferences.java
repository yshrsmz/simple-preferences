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

  /**
   * whether or not create additional clear method which uses SharedPreferences.Editor#commit().
   * this is useful when you want to write to the backing file synchronously.
   *
   * @return true if you need synchronous method
   */
  boolean needCommitMethodForClear() default false;


  /**
   * whether to make generated classes public or not
   *
   * @return true if generated class should be public. If false, the class will be package private.
   */
  boolean expose() default true;
}
