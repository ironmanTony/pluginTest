package com.talbot.plugintest;

import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by tao.li on 2019/1/20.
 */
public class AMSHookHelper {

  public static final String TAG = "AMSHookHelper";
  public static final String EXTRA_TARGET_INTENT = "IActivityManagerHandler";

  public static void hookAms() {
    try {
      Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManager");

      Field gDefaultField = activityManagerNativeClass.getDeclaredField("IActivityManagerSingleton");
      gDefaultField.setAccessible(true);

      Object gDefault = gDefaultField.get(null);
      Class<?> singleton = Class.forName("android.util.Singleton");
      Field mInstanceField = singleton.getDeclaredField("mInstance");
      mInstanceField.setAccessible(true);

      Object rawIActivityManager = mInstanceField.get(gDefault);

      Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
      Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class<?>[]{iActivityManagerInterface}, new IActivityManagerHandler(rawIActivityManager));
      mInstanceField.set(gDefault, proxy);
    } catch (ClassNotFoundException e) {
      Log.e(TAG, "", e);
    } catch (IllegalAccessException e) {
      Log.e(TAG, "", e);
    } catch (NoSuchFieldException e) {
      Log.e(TAG, "", e);
    }
  }

  public static void replaceAgain() {
    try {
      Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
      Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
      currentActivityThreadField.setAccessible(true);
      Object currentActivityThread = currentActivityThreadField.get(null);

      Field mHField = activityThreadClass.getDeclaredField("mH");
      mHField.setAccessible(true);
      Handler mH = (Handler) mHField.get(currentActivityThread);
      Field mCallBackField = Handler.class.getDeclaredField("mCallback");
      mCallBackField.setAccessible(true);

      mCallBackField.set(mH, new ActivityThreadHandlerCallback(mH));
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }
}
