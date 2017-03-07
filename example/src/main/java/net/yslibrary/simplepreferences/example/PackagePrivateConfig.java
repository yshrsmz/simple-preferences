package net.yslibrary.simplepreferences.example;

import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;

/**
 * Created by a12897 on 2017/03/07.
 */
@Preferences(expose = false)
abstract class PackagePrivateConfig {
  @Key
  protected int userId = 0;

  @Key
  protected String userName = "";

  @Key(omitGetterPrefix = true)
  protected boolean isPremium = false;
}
