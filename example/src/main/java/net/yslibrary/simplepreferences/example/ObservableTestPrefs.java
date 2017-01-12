package net.yslibrary.simplepreferences.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Cancellable;
import rx.functions.Func1;

/**
 * Created by yshrsmz on 16/10/12.
 */
public class ObservableTestPrefs {

  private final SharedPreferences prefs;
  private final Observable<String> keyChanges;
  protected String userId = "";

  private ObservableTestPrefs(@NonNull Context context) {
    prefs = context.getApplicationContext().getSharedPreferences("observable_test", Context.MODE_PRIVATE);

    keyChanges = Observable.fromEmitter(new Action1<Emitter<String>>() {
      @Override
      public void call(final Emitter<String> stringEmitter) {
        final SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
          @Override
          public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            stringEmitter.onNext(s);
          }
        };

        stringEmitter.setCancellation(new Cancellable() {
          @Override
          public void cancel() throws Exception {
            prefs.unregisterOnSharedPreferenceChangeListener(listener);
          }
        });
      }
    }, Emitter.BackpressureMode.BUFFER).share();
  }

  public static ObservableTestPrefs create(@NonNull Context context) {
    if (context == null) {
      throw new NullPointerException("Context is Null!");
    }
    return new ObservableTestPrefs(context);
  }

  public Observable<String> getUserIdAsObservable() {
    return keyChanges
        .filter(new Func1<String, Boolean>() {
          @Override
          public Boolean call(String s) {
            return "user_id".equals(s);
          }
        })
        .startWith("<init>") // trigger initial load
        .onBackpressureLatest()
        .map(new Func1<String, String>() {
          @Override
          public String call(String s) {
            return prefs.getString("user_id", userId);
          }
        });
  }

  public Observable<String> getUserIdAsObservable(final String defaultValue) {
    return keyChanges
        .filter(new Func1<String, Boolean>() {
          @Override
          public Boolean call(String s) {
            return "user_id".equals(s);
          }
        })
        .startWith("<init>") // trigger initial load
        .onBackpressureLatest()
        .map(new Func1<String, String>() {
          @Override
          public String call(String s) {
            return prefs.getString("user_id", defaultValue);
          }
        });
  }
}
