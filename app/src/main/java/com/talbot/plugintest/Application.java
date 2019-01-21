package com.talbot.plugintest;

import android.content.Context;

/**
 * Created by tao.li on 2019/1/20.
 */
public class Application extends android.app.Application {
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);

    AMSHookHelper.hookAms();
    AMSHookHelper.replaceAgain();
  }
}
