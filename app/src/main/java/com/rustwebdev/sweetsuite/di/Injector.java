package com.rustwebdev.sweetsuite.di;


import android.app.Application;

public class Injector {
  // AppComponent may need to be init() from either App, Service or Receiver and must be the same instance (leave as AppComponent?)
  private static AppComponent appComponent;

  public static void init(Application app) {
    if (appComponent == null) {
      appComponent = DaggerAppComponent.builder().appModule(new AppModule(app)).build();
    }
  }

  public static AppComponent get() throws IllegalStateException {
    return appComponent;
  }

}
