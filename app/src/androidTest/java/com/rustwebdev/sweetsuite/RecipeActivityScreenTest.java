package com.rustwebdev.sweetsuite;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.contrib.ViewPagerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.rustwebdev.sweetsuite.ui.recipes.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by flanhelsinki on 10/26/17.
 */
@RunWith(AndroidJUnit4.class) public class RecipeActivityScreenTest {

  private static final String STEP_NAME = "1. Starting prep";
  @Rule public final ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);
  private IdlingResource mIdlingResource;

  @Before public void registerIdlingResource() {
    mIdlingResource = (IdlingResource) mActivityTestRule.getActivity().getIdlingResource();
    Espresso.registerIdlingResources(mIdlingResource);
  }

  @Test public void checkViewPagerSwipe() {
    // First scroll to the position that needs to be matched and click on it.
    onView(ViewMatchers.withId(R.id.recipe_rv)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.pager)).perform(ViewPagerActions.scrollRight());
    onView(withId(R.id.nav_recipe_step)).check(matches(withText(STEP_NAME)));
  }

  @After public void unregisterIdlingResource() {
    if (mIdlingResource != null) {
      Espresso.unregisterIdlingResources(mIdlingResource);
    }
  }
}
