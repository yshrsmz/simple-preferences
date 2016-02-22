package net.yslibrary.simplepreferences.processor;

import com.google.common.base.CaseFormat;

/**
 * Created by yshrsmz on 2016/02/22.
 */
public class Utils {

  public static String lowerToUpperCamel(String name) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
  }

  public static String lowerCamelToLowerSnake(String name) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
  }
}
