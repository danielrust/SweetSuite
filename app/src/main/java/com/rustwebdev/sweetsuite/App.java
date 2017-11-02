package com.rustwebdev.sweetsuite;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.rustwebdev.sweetsuite.di.AppComponent;
import com.rustwebdev.sweetsuite.di.AppModule;
import com.rustwebdev.sweetsuite.di.DaggerAppComponent;
import com.rustwebdev.sweetsuite.di.Injector;

public class App extends Application {
  private AppComponent component;

  @Override public void onCreate() {
    super.onCreate();
    Injector.init(this);
    Stetho.initializeWithDefaults(this);
    component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
  }
  public AppComponent getComponent(){
    return component;
  }
}

