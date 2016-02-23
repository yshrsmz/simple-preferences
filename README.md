Simple Preferences
===

Android Library to simplify SharedPreferences use.


define ordinary class

```
@Preferences
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

  @Key
  boolean booleanValue;

  @Key(omitGetterPrefix = true)
  boolean someBooleanValue;
}
```


and following class is generated

```
public class SettingsPrefs extends Settings {
  private final SharedPreferences prefs;

  private SettingsPrefs(Context context) {
    if (context == null) {
      throw new NullPointerException("Context is Null!");
    }
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static SettingsPrefs create(Context context) {
    if (context == null) {
      throw new NullPointerException("Context is Null!");
    }
    return new SettingsPrefs(context);
  }

  public void clear() {
    prefs.edit().clear().apply();
  }

  public void setUserId(int value) {
    prefs.edit().putInt("user_id", value).apply();
  }

  public int getUserId() {
    return prefs.getInt("user_id", userId);
  }

  public boolean hasUserId() {
    return prefs.contains("user_id");
  }

  public void removeUserId() {
    prefs.edit().remove("user_id").apply();
  }

  public void setUserName(String value) {
    prefs.edit().putString("user_name", value).apply();
  }

  public String getUserName() {
    return prefs.getString("user_name", userName);
  }

  public boolean hasUserName() {
    return prefs.contains("user_name");
  }

  public void removeUserName() {
    prefs.edit().remove("user_name").apply();
  }

  public void setLongValue(long value) {
    prefs.edit().putLong("long_value", value).apply();
  }

  public long getLongValue() {
    return prefs.getLong("long_value", longValue);
  }

  public boolean hasLongValue() {
    return prefs.contains("long_value");
  }

  public void removeLongValue() {
    prefs.edit().remove("long_value").apply();
  }

  public void setFloatValue(float value) {
    prefs.edit().putFloat("float_value", value).apply();
  }

  public float getFloatValue() {
    return prefs.getFloat("float_value", floatValue);
  }

  public boolean hasFloatValue() {
    return prefs.contains("float_value");
  }

  public void removeFloatValue() {
    prefs.edit().remove("float_value").apply();
  }

  public void setStringSetValue(Set<String> value) {
    prefs.edit().putStringSet("string_set_value", value).apply();
  }

  public Set<String> getStringSetValue() {
    return prefs.getStringSet("string_set_value", stringSetValue);
  }

  public boolean hasStringSetValue() {
    return prefs.contains("string_set_value");
  }

  public void removeStringSetValue() {
    prefs.edit().remove("string_set_value").apply();
  }

  public void setBooleanValue(boolean value) {
    prefs.edit().putBoolean("boolean_value", value).apply();
  }

  public boolean isBooleanValue() {
    return prefs.getBoolean("boolean_value", booleanValue);
  }

  public boolean hasBooleanValue() {
    return prefs.contains("boolean_value");
  }

  public void removeBooleanValue() {
    prefs.edit().remove("boolean_value").apply();
  }

  public void setSomeBooleanValue(boolean value) {
    prefs.edit().putBoolean("some_boolean_value", value).apply();
  }

  public boolean someBooleanValue() {
    return prefs.getBoolean("some_boolean_value", someBooleanValue);
  }

  public boolean hasSomeBooleanValue() {
    return prefs.contains("some_boolean_value");
  }

  public void removeSomeBooleanValue() {
    prefs.edit().remove("some_boolean_value").apply();
  }
}
```

Generated classes will be placed at the same package as parent abstract classes.