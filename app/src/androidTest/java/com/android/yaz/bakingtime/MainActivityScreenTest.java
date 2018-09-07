package com.android.yaz.bakingtime;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    private static final String RECIPE_NAME = "Nutella Pie";
    private static final String RECIPE_FIRST_STEP = "Recipe Introduction";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;
    private IdlingRegistry mIdlingRegistry = IdlingRegistry.getInstance();

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        mIdlingRegistry.register(mIdlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpensDetailActivity() {

        onView(withId(R.id.baking_recycler_view))
                .perform(RecyclerViewActions.<RecipesAdapter.RecipesAdapterViewHolder>actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recipe_name_text_view)).check(matches(withText(RECIPE_NAME)));
    }

    @Test
    public void clickRecyclerViewItem_DisplaysIngredients() {

        onView(withId(R.id.baking_recycler_view))
                .perform(RecyclerViewActions.<RecipesAdapter.RecipesAdapterViewHolder>actionOnItemAtPosition(0, click()));

        onView(withId(R.id.ingredients_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void clickStepsRecyclerViewItem_DisplaysStepDescription() {

        onView(withId(R.id.baking_recycler_view))
                .perform(RecyclerViewActions.<RecipesAdapter.RecipesAdapterViewHolder>actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recipe_steps_recycler_view))
                .perform(RecyclerViewActions.<RecipeStepsAdapter.RecipeStepsAdapterViewHolder>actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recipe_step_description_tv)).check(matches(withText(RECIPE_FIRST_STEP)));
    }

    @After
    public void unregisterIdlingResource() {
        if(mIdlingResource != null) {
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }

}
