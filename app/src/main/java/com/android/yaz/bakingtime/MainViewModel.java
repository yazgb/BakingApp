package com.android.yaz.bakingtime;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.android.yaz.bakingtime.model.Recipe;
import com.android.yaz.bakingtime.utilities.BakingWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private MutableLiveData<List<Recipe>> recipeList;

    public LiveData<List<Recipe>> getRecipeList() {

        if(recipeList == null) {
            recipeList = new MutableLiveData<List<Recipe>>();
            loadRecipes();
        }
        return recipeList;
    }

    private void loadRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BakingWebService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BakingWebService bakingWebService = retrofit.create(BakingWebService.class);
        Call<List<Recipe>> call = bakingWebService.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}
