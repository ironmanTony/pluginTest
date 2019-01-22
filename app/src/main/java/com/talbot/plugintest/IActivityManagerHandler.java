package com.talbot.plugintest;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static com.talbot.plugintest.AMSHookHelper.EXTRA_TARGET_INTENT;
import static com.talbot.plugintest.AMSHookHelper.TAG;

/**
 * Created by tao.li on 2019/1/20.
 */
class IActivityManagerHandler implements InvocationHandler {



  private Object mBase;

  public IActivityManagerHandler(Object rawIActivityManager) {
    this.mBase = rawIActivityManager;

  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if ("startActivity".equals(method.getName())) {
      Intent raw;
      int index = 0;

      for (int i = 0; i < args.length; i++) {
        if (args[i] instanceof Intent) {
          index = i;
          break;
        }
      }
      raw = (Intent) args[index];
      Intent newIntent = new Intent();
      String stubPackage = "com.talbot.plugintest";

      ComponentName componentName = new ComponentName(stubPackage, Main3Activity.class.getName());
      newIntent.setComponent(componentName);

      newIntent.putExtra(EXTRA_TARGET_INTENT, raw);
      args[index] = newIntent;

      Log.d(TAG, "hook success");
      return method.invoke(mBase, args);

    }

    return method.invoke(mBase, args);
  }
}
