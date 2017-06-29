package com.temunide.capstoneproject.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.temunide.capstoneproject.R;
import com.temunide.capstoneproject.StoryListActivity;

/**
 * Helper class for showing and canceling utils
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
class NotificationUtils {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "Utils";


        static void notify(final Context context,
                              final Story story, final int number) {
        final Resources res = context.getResources();

        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_round);
        RemoteViews content = new RemoteViews(context.getPackageName(),R.layout.widget_list_layout);
        content.setTextViewText(R.id.story_title,story.getTitle());
        content.setTextViewText(R.id.story_author,story.getAuthor());
        RemoteViews bigContent  = new RemoteViews(content.getPackage(),R.layout.notification_big_view);
        bigContent.setTextViewText(R.id.story_title,story.getTitle());
        bigContent.setTextViewText(R.id.story_content,story.getStory());
        bigContent.setTextViewText(R.id.story_author,story.getAuthor());
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setCustomContentView(content)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setCustomBigContentView(bigContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(picture)
                .setNumber(number)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context, StoryListActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setStyle( new NotificationCompat.BigTextStyle().bigText(story.getAuthor()).setSummaryText(story.getStory()).setBigContentTitle(story.getTitle()))
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
