package net.yslibrary.simplepreferences;

import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yshrsmz on 2016/02/24.
 */
@Preferences
public class ValidPrefsWithDefaultName {

  @Key
  protected String stringValue = "";

  @Key
  protected int intValue = 0;

  @Key
  protected float floatValue = 0f;

  @Key
  protected long longValue = 0L;

  @Key
  protected Set<String> stringSetValue = new HashSet<>();

  @Key(name = "c_s_v")
  protected String customStringValue = "";

  @Key(name = "c_i_v")
  protected int customIntValue = 0;

  @Key(name = "c_f_v")
  protected float customFloatValue = 0f;

  @Key(name = "c_l_v")
  protected long customLongValue = 0L;

  @Key(name = "c_ss_v")
  protected Set<String> customStringSetValue = new HashSet<>();
}
