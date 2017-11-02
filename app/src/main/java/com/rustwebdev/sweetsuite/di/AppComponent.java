package com.rustwebdev.sweetsuite.di;

import android.app.Application;
import com.rustwebdev.sweetsuite.App;
import com.rustwebdev.sweetsuite.datasource.webservice.ServiceModule;
import com.rustwebdev.sweetsuite.recipe.RecipeActivity;
import com.rustwebdev.sweetsuite.ui.recipes.MainActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, ServiceModule.class })
public interface AppComponent {
  void inject(App application);

  void inject(MainActivity target);

  void inject(RecipeActivity target);

  public Application application();
}

