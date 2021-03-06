package com.temunide.capstoneproject.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.temunide.capstoneproject.R;
import com.temunide.capstoneproject.SignInActivity;

/**
 * Implementation of App Widget functionality.
 */
public class LatestPostsWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.latest_posts_widget);
        Intent adapterIntent = new Intent(context, LatestPostsService.class);
        views.setRemoteAdapter(R.id.story_list, adapterIntent);
        views.setEmptyView(R.id.story_list, R.id.empty_view);
        Intent intent = new Intent(context, SignInActivity.class);
        views.setOnClickPendingIntent(R.id.widget_layout, PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        views.setPendingIntentTemplate(R.id.story_list, PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
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

