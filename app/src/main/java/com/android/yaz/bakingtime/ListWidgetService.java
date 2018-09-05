package com.android.yaz.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.yaz.bakingtime.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ListWidgetService extends RemoteViewsService{
    private static final String TAG = ListWidgetService.class.getSimpleName();
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext());
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = ListRemoteViewFactory.class.getSimpleName();

    private Context mContext;

    private int mNumIngredients;
    private List<String> mIngredients = new ArrayList<String>();


    public ListRemoteViewFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mIngredients.clear();
        mNumIngredients = 0;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), mContext.MODE_PRIVATE);

        if(sharedPreferences.contains(mContext.getString(R.string.recipe_name_key))) {

            mNumIngredients = sharedPreferences.getInt(mContext.getString(R.string.recipe_number_ingredients_key),0);

            String ingredientName;

            for(int i = 0; i < mNumIngredients; i++) {
                ingredientName = sharedPreferences.getString(mContext.getString(R.string.ingredient_key,i), null);

                if(ingredientName!=null && !ingredientName.isEmpty()) {
                    mIngredients.add(ingredientName);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if(mIngredients == null) {
            return 0;
        }
        else {
            return mIngredients.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.widget_item, mIngredients.get(i));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
