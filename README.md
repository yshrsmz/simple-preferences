Simple Preferences
===

Android Library to simplify SharedPreferences use.


define abstract class

```
@Preferences("settings")
public abstract Settings {
    @Key String userName;
    @Key boolean admin;
}
```


and following class is generated

```
public class SettingsPrefs extends Settings {

    public SettingsPrefs(Context context) {

    }

    public String getUserName() {
    }

    public void setUserName(String userName)  {
    }

    public boolean hasUserName() {
    }

    public void removeUserName() {
    }

    public boolean isAdmin() {
    }

    public boolean setAdmin(boolean admin) {
    }

    public boolean hasAdmin() {
    }

    public void removeAdmin() {
    }

    public void clear() {
    }

    public static SettingsPrefs create(Context context) {
    }
}
```

Generated classes will be placed at the same package as parent abstract classes.