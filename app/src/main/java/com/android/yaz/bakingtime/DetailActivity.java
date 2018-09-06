package com.android.yaz.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.yaz.bakingtime.model.Ingredient;
import com.android.yaz.bakingtime.model.Recipe;
import com.android.yaz.bakingtime.model.RecipeStep;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    protected static final String RECIPE_SELECTED = "recipeSelected";
    protected static final String STEP_INDEX_SELECTED = "stepIndexSelected";
    protected static final String TWO_PANE = "twoPane";

    private Recipe mRecipe;
    private boolean mTwoPane;

    @BindView(R.id.recipe_name_text_view) TextView mRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Bundle mBundle = getIntent().getExtras();
        if(mBundle != null && mBundle.containsKey(MainActivity.CLICKED_RECIPE)) {
            mRecipe = mBundle.getParcelable(MainActivity.CLICKED_RECIPE);

            mRecipeName.setText(mRecipe.getName());
        }

        if(findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {

                if(mBundle != null) {
                    Recipe recipe = mBundle.getParcelable(MainActivity.CLICKED_RECIPE);
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
            mTwoPane = false;
        }
    }

    public void onRecipeStepSelected(Recipe recipe, int stepIndex) {

        if(mTwoPane) {
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
            Intent intent = new Intent(DetailActivity.this, RecipeStepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPE_SELECTED, recipe);
            bundle.putInt(STEP_INDEX_SELECTED, stepIndex);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        if (sharedPreferences.contains(getString(R.string.recipe_name_key))) {

            String recipeString = sharedPreferences.getString(getString(R.string.recipe_name_key), null);
            if (recipeString.contentEquals(mRecipe.getName())) {
                menu.getItem(0).setChecked(true);
            } else {
                menu.getItem(0).setChecked(false);
            }
        } else {
            menu.getItem(0).setChecked(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_desired_recipe) {

            if(item.isChecked())
            {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(item.isChecked()) {
                editor.putString(getString(R.string.recipe_name_key), mRecipe.getName());
                Ingredient[] ingredients = mRecipe.getIngredients();
                editor.putInt(getString(R.string.recipe_number_ingredients_key), ingredients.length );
                String ingredientStr;
                for(int i = 0; i < ingredients.length; i++ ) {
                    ingredientStr = ingredients[i].getQuantity() + " " + ingredients[i].getMeasure() + " " + ingredients[i].getIngredient();
                    editor.putString(getString(R.string.ingredient_key,i), ingredientStr );
                }
                editor.apply();
            } else {
                editor.clear();
                editor.apply();
            }

            updateAppWidget();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateAppWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetsId = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), BakingTimeWidgetProvider.class ));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetsId, R.id.list_view);

        Intent intent = new Intent(getApplicationContext(), BakingTimeWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetsId);
        sendBroadcast(intent);
    }
}
