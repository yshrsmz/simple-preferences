package net.yslibrary.simplepreferences.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by yshrsmz on 16/10/12.
 */
public class ObservableTestPrefs {

  private final SharedPreferences prefs;

  private ObservableTestPrefs(@NonNull Context context) {
    prefs = context.getApplicationContext().getSharedPreferences("observable_test", Context.MODE_PRIVATE);
  }
}
