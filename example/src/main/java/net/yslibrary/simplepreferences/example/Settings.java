package net.yslibrary.simplepreferences.example;

import java.util.Set;
import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;

/**
 * Created by yshrsmz on 2016/02/22.
 */
@Preferences("settings")
public class Settings {
  @Key
  int userId;

  @Key
  String userName;

  @Key
  long longValue;

  @Key
  float floatValue;

  @Key
  Set<String> stringSetValue;

  @Key(name = "specified_boolean_key")
  boolean booleanValue;

  @Key(omitGetterPrefix = true)
  boolean someBooleanValue;

  @Key(omitGetterPrefix = true, needCommitMethod = true)
  boolean someBooleanValueForSyncOperation;

  @Key(needCommitMethod = true)
  String someStringValueForSyncOperation;
}
