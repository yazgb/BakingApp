package com.android.yaz.bakingtime;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.yaz.bakingtime.IdlingResource.SimpleIdlingResource;
import com.android.yaz.bakingtime.model.Recipe;
import com.android.yaz.bakingtime.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.baking_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.recipes_error_tv) TextView mErrorMessage;

    @Nullable private SimpleIdlingResource mIdlingResource;

    private GridLayoutManager mGridLayoutManager;
    private RecipesAdapter mRecipesAdapter;

    protected static final String CLICKED_RECIPE = "clickedRecipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupUI();
    }

    protected void setupUI() {

        int numOfColumns = Utility.calculateNoOfColumns(this);
        mGridLayoutManager = new GridLayoutManager(this, numOfColumns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecipesAdapter = new RecipesAdapter(MainActivity.this, null, MainActivity.this);
        mRecyclerView.setAdapter(mRecipesAdapter);

        getIdlingResource();
        if(mIdlingResource != null)
            mIdlingResource.setIdleState(false);

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getRecipeList().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes != null) {
                    showRecipesGrid();
                    mRecipesAdapter.setRecipesData(recipes);
                    if(mIdlingResource != null)
                        mIdlingResource.setIdleState(true);
                }
                else
                {
                    showErrorMessage();
                }
            }
        });
    }

    @Override
    public void itemClick(Recipe clickedRecipe) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CLICKED_RECIPE, clickedRecipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showRecipesGrid() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if(mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
