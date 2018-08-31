package com.android.yaz.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.yaz.bakingtime.model.Ingredient;
import com.android.yaz.bakingtime.model.Recipe;
import com.android.yaz.bakingtime.model.RecipeStep;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterRecipeDetailsFragment extends Fragment implements RecipeStepsAdapter.RecipeStepClickListener{

    private static final String TAG = MasterRecipeDetailsFragment.class.getSimpleName();

    @BindView(R.id.ingredients_recycler_view) RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.recipe_steps_recycler_view) RecyclerView mRecipeStepsRecyclerView;

    private Recipe mRecipe;

    public MasterRecipeDetailsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "ON_CREATE_VIEW MasterRecipeDetailsFragment");

        final View rootView = inflater.inflate(R.layout.fragment_master_recipe_details, container, false);
        ButterKnife.bind(this, rootView);

        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null) {

            Intent intent = getActivity().getIntent();
            if (intent != null) {

                Bundle originBundle = intent.getExtras();
                if (originBundle != null) {

                    mRecipe = originBundle.getParcelable(MainActivity.CLICKED_RECIPE);

                    if (mRecipe != null) {
                        Ingredient[] ingredients = mRecipe.getIngredients();
                        RecipeStep[] recipeSteps = mRecipe.getSteps();

                        mIngredientsRecyclerView.setHasFixedSize(true);
                        LinearLayoutManager mIngredientsLinearLayoutManager = new LinearLayoutManager(this.getActivity());
                        mIngredientsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mIngredientsRecyclerView.setLayoutManager(mIngredientsLinearLayoutManager);
                        IngredientsAdapter mIngredientsAdapter = new IngredientsAdapter(ingredients);
                        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);

                        mRecipeStepsRecyclerView.setHasFixedSize(true);
                        LinearLayoutManager mStepsLinearLayoutManager = new LinearLayoutManager(this.getActivity());
                        mStepsLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mRecipeStepsRecyclerView.setLayoutManager(mStepsLinearLayoutManager);
                        RecipeStepsAdapter mRecipeStepsAdapter = new RecipeStepsAdapter(recipeSteps, this);
                        mRecipeStepsRecyclerView.setAdapter(mRecipeStepsAdapter);
                    }
                }
            }
        }

        return rootView;
    }

    @Override
    public void stepClick(int stepIndex) {

        ((DetailActivity) getActivity()).onRecipeStepSelected(mRecipe, stepIndex);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "ON_DESTROY MasterRecipeDetailsFragment");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "ON_PAUSE MasterRecipeDetailsFragment");
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "ON_CREATE MasterRecipeDetailsFragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "ON_RESUME MasterRecipeDetailsFragment");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "ON_START MasterRecipeDetailsFragment");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "ON_STOP MasterRecipeDetailsFragment");
        super.onStop();
    }
}