package com.rustwebdev.sweetsuite;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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
 * Created by flanhelsinki on 10/25/17.
 */

@RunWith(AndroidJUnit4.class) public class MainActivityScreenTest {
  private static final String RECIPE_NAME = "Nutella Pie";
  @Rule public final ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);
  private IdlingResource mIdlingResource;

  @Before public void registerIdlingResource() {
    mIdlingResource = (IdlingResource) mActivityTestRule.getActivity().getIdlingResource();
    Espresso.registerIdlingResources(mIdlingResource);
  }

  @Test public void checkRecyclerView_openRecipeFragment() {
    // First scroll to the position that needs to be matched and click on it.
    onView(ViewMatchers.withId(R.id.recipe_rv)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.nav_recipe_title)).check(matches(withText(RECIPE_NAME)));
  }

  @After public void unregisterIdlingResource() {
    if (mIdlingResource != null) {
      Espresso.unregisterIdlingResources(mIdlingResource);
    }
  }
}
