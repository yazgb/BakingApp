package com.android.yaz.bakingtime;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.yaz.bakingtime.model.Recipe;
import com.android.yaz.bakingtime.model.RecipeStep;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    protected static final String RECIPE_SELECTED = "recipeSelected";
    protected static final String STEP_INDEX_SELECTED = "stepIndexSelected";
    protected static final String TWO_PANE = "twoPane";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "ON_CREATE DETAIL_ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(findViewById(R.id.step_detail_container) != null) {
            Log.d(TAG, "TWO PANE");
            mTwoPane = true;

            if(savedInstanceState == null) {

                Bundle bundle = getIntent().getExtras();
                if(bundle != null) {
                    Recipe recipe = bundle.getParcelable(MainActivity.CLICKED_RECIPE);
                    int index = 0;

                    if(recipe != null) {
                        RecipeStep[] mStepsArray = recipe.getSteps();

                        RecipeStep mRecipeStep = recipe.getSteps()[index];

                        RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();

                        stepDetailFragment.setStepsArray(mStepsArray);
                        stepDetailFragment.setRecipeStepIndex(index);
                        stepDetailFragment.setVideoURL(mRecipeStep.getVideoURL());
                        stepDetailFragment.setStepDescription(mRecipeStep.getDescription());

                        Bundle b = new Bundle();
                        b.putBoolean(TWO_PANE, true);
                        stepDetailFragment.setArguments(b);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.step_detail_container, stepDetailFragment)
                                .commit();
                    }
                }
            }
        }
        else {
            Log.d(TAG, "ONE PANE");
            mTwoPane = false;
        }
    }

    public void onRecipeStepSelected(Recipe recipe, int stepIndex) {

        if(mTwoPane) {
            Log.d(TAG, "REFRESH FRAGMENT VIDEO");
            RecipeStepDetailFragment fragment = (RecipeStepDetailFragment) getSupportFragmentManager().getFragments().get(1);

            if(recipe != null) {
                RecipeStep[] recipeSteps = recipe.getSteps();
                fragment.setVideoURL(recipeSteps[stepIndex].getVideoURL());
                fragment.setStepDescription(recipeSteps[stepIndex].getDescription());
            } else {
                fragment.setVideoURL("");
                fragment.setStepDescription("");
            }
            fragment.setRecipeStepIndex(stepIndex);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().detach(fragment).attach(fragment).commit();

        } else {
            Log.d(TAG, "INTENT TO RecipeStepDetailActivity");
            Intent intent = new Intent(DetailActivity.this, RecipeStepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPE_SELECTED, recipe);
            bundle.putInt(STEP_INDEX_SELECTED, stepIndex);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "ON_RESUME DETAIL ACTIVITY");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "ON_PAUSE DETAIL ACTIVITY");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "ON_DESTROY DETAIL ACTIVITY");
        super.onDestroy();
    }
}
