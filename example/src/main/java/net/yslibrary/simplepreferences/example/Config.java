package net.yslibrary.simplepreferences.example;

import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;

/**
 * Created by yshrsmz on 2016/02/26.
 */
@Preferences(needCommitMethodForClear = true)
public class Config {

  @Key
  protected int userId = 0;

  @Key
  protected String userName = "";

  @Key(omitGetterPrefix = true)
  protected boolean isPremium = false;
}
