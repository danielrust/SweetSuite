package com.rustwebdev.sweetsuite.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.rustwebdev.sweetsuite.datasource.database.main.MainDatabase;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.IngredientDao;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.RecipeDao;
import com.rustwebdev.sweetsuite.datasource.database.main.step.StepDao;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {

  private Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton public Application provideApplication() {
    return application;
  }

  @Provides @Singleton public Context provideContext() {
    return application;
  }

  @Provides @Singleton public MainDatabase provideMainDatabase(Application application) {
    return Room.databaseBuilder(application, MainDatabase.class, MainDatabase.DATABASE_NAME)
        .build();
  }

  @Provides @Singleton public RecipeDao provideRecipeDao(MainDatabase mainDatabase) {
    return mainDatabase.recipeDao();
  }

  @Provides @Singleton public StepDao provideStepDao(MainDatabase mainDatabase) {
    return mainDatabase.stepDao();
  }

  @Provides @Singleton public IngredientDao provideIngredientDao(MainDatabase mainDatabase) {
    return mainDatabase.ingredientDao();
  }
}
