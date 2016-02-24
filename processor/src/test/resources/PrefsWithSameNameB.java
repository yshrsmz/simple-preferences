package net.yslibrary.simplepreferences.processor;

import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;

/**
 * Created by yshrsmz on 2016/02/24.
 */
@Preferences("custom_name")
public class PrefsWithSameNameB {
  @Key
  String someValue = "";
}
