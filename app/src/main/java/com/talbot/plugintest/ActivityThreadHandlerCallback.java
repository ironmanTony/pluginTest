package com.talbot.plugintest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;

/**
 * Created by tao.li on 2019/1/20.
 */
class ActivityThreadHandlerCallback implements Handler.Callback {

  private Handler mBase;

  public ActivityThreadHandlerCallback(Handler mH) {
    this.mBase = mH;
  }

  @Override
  public boolean handleMessage(Message msg) {
    switch (msg.what) {
      case 100://100对应了ActivityThread的LAUNCH_ACTIVITY
        handleLaunchActivity(msg);
        break;
    }
    mBase.handleMessage(msg);
    return true;
  }

  private void handleLaunchActivity(Message msg) {

    Object obj = msg.obj;
    try {
      Field intent = obj.getClass().getDeclaredField("intent");
      intent.setAccessible(true);
      Intent raw = (Intent) intent.get(obj);
      Intent target = raw.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
      raw.setComponent(target.getComponent());

    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
