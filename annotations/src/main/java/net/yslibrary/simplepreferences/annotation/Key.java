package net.yslibrary.simplepreferences.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yshrsmz on 2016/02/20.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Key {
  /**
   * name for the preference field.
   * if empty, use lower-cased variable name as its preference field name
   *
   * @return preference field name
   */
  String name() default "";

  /**
   * whether or not put get/is prefix as setter method's prefix.
   * in some case, you would prefer to use variable name as a getter name.
   *
   * @return true if you use variable name as a name for its setter method
   */
  boolean omitGetterPrefix() default false;

  /**
   * whether or not create additional setter/remover methods which use SharedPreferences.Editor#commit().
   * this is useful when you want to write to the backing file synchronously.
   *
   * @return true if you want synchronous method
   */
  boolean needCommitMethod() default false;

  /**
   * specify getters you need
   *
   * <dl>
   *   <dt>{@link net.yslibrary.simplepreferences.annotation.Key.GetterType#GETTER}</dt>
   *   <dd>generate simple getter. this is default behavior.</dd>
   *   <dt>{@link net.yslibrary.simplepreferences.annotation.Key.GetterType#OBSERVABLE}</dt>
   *   <dd>generate method which returns rx.Observable. this observable will be notified on value change. this method is suffixed as `asObservable`</dd>
   *   <dt>{@link net.yslibrary.simplepreferences.annotation.Key.GetterType#GETTER_AND_OBSERVABLE}</dt>
   *   <dd>generate both of above method</dd>
   * </dl>
   *
   * @return
   */
  GetterType getterType() default GetterType.GETTER;


  enum GetterType {
    GETTER,
    OBSERVABLE,
    GETTER_AND_OBSERVABLE
  }
}
