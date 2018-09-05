package com.android.yaz.bakingtime;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.yaz.bakingtime.model.Ingredient;
import com.android.yaz.bakingtime.model.Recipe;
import com.android.yaz.bakingtime.model.RecipeStep;

public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        if(savedInstanceState == null) {
            Bundle mBundle = getIntent().getExtras();
            if(mBundle!=null) {
                Recipe mRecipe = mBundle.getParcelable(DetailActivity.RECIPE_SELECTED);
                int mIndex = mBundle.getInt(DetailActivity.STEP_INDEX_SELECTED);

                if(mRecipe != null) {
                    RecipeStep[] mStepsArray = mRecipe.getSteps();

                    RecipeStep mRecipeStep = mRecipe.getSteps()[mIndex];

                    RecipeStepDetailFragment mStepDetailFragment = new RecipeStepDetailFragment();

                    mStepDetailFragment.setStepsArray(mStepsArray);
                    mStepDetailFragment.setRecipeStepIndex(mIndex);
                    mStepDetailFragment.setVideoURL(mRecipeStep.getVideoURL());
                    mStepDetailFragment.setStepDescription(mRecipeStep.getDescription());

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.step_detail_container, mStepDetailFragment)
                            .commit();
                }
            }
        }
    }


    public void replaceRecipeStep(RecipeStep[] steps, int stepIndex) {

        RecipeStepDetailFragment fragment = (RecipeStepDetailFragment) getSupportFragmentManager().getFragments().get(0);

        if(steps != null) {
            fragment.setVideoURL(steps[stepIndex].getVideoURL());
            fragment.setStepDescription(steps[stepIndex].getDescription());
        } else {
            fragment.setVideoURL("");
            fragment.setStepDescription("");
        }
        fragment.setRecipeStepIndex(stepIndex);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().detach(fragment).attach(fragment).commit();
    }
}
