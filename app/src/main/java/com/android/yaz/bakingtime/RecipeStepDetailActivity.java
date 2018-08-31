package com.android.yaz.bakingtime;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.yaz.bakingtime.model.Recipe;
import com.android.yaz.bakingtime.model.RecipeStep;

public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "ON_CREATE_RecipeStepDetailActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        if(savedInstanceState == null) {
            Log.d(TAG, "ACTIVITY savedInstanceState NULL");
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
        } else {
            Log.d(TAG, "ACTIVITY savedInstanceState NO NULL");
        }
    }


    public void replaceRecipeStep(RecipeStep[] steps, int stepIndex) {

        Log.d(TAG, "replaceRecipeStep NEW INDEX: " + stepIndex);
        Log.d(TAG, steps!=null? "# of steps: "+ steps.length : "steps array es null");

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
