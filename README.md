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

    public void putUserName(String userName)  {
    }

    public boolean hasUserName() {
    }

    public void removeUserName() {
    }

    public boolean isAdmin() {
    }

    public boolean putAdmin(boolean admin) {
    }

    public boolean hasAdmin() {
    }

    public void removeAdmin() {
    }

    public void clearPrefs() {
    }
}
```

Generated classes will be placed at the same package as parent abstract classes.