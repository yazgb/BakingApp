package com.android.yaz.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingTimeWidgetProvider extends AppWidgetProvider {

    private final static String TAG = BakingTimeWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_widget_provider);


        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), MODE_PRIVATE);
        if(sharedPreferences.contains(context.getString(R.string.recipe_name_key))) {
            views.setTextViewText(R.id.recipe_name_widget_tv, sharedPreferences.getString(context.getString(R.string.recipe_name_key),""));
        } else
        {
            views.setTextViewText(R.id.recipe_name_widget_tv, context.getString(R.string.baking_time));
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        Intent intentList = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.list_view, intentList);

        views.setEmptyView(R.id.list_view, R.id.empty_view);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

