/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gcm.demo.app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";
	private static final String YES_ACTION = "com.google.android.gcm.demo.app.intent.action.YES";
	private static final String MAYBE_ACTION = "com.google.android.gcm.demo.app.intent.action.MAYBE";
	private static final String NO_ACTION = "com.google.android.gcm.demo.app.intent.action.NO";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification("Received: " + extras.getString("data"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.notiication);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DemoActivity.class), 0);
        
        String url = "tel:3334444";
        PendingIntent callIntent = PendingIntent.getActivity(this, 0,
                new Intent(Intent.ACTION_CALL, Uri.parse(url)), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        
        
       
        Intent yesReceive = new Intent(this,DemoActivity.class);  
        yesReceive.setAction(YES_ACTION);
        PendingIntent pendingIntentYes = PendingIntent.getActivity(this, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        //Maybe intent
        Intent maybeReceive = new Intent(android.content.Intent.ACTION_SEND);  
        maybeReceive.setType("plain/text");    
        maybeReceive.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"abc@xyz.com"});
        maybeReceive.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sending Email");   
        maybeReceive.putExtra(android.content.Intent.EXTRA_TEXT, "Sending Email");
        PendingIntent pendingIntentMaybe = PendingIntent.getActivity(this, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        //No intent
        Intent noReceive = new Intent();  
        noReceive.setAction(NO_ACTION);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(this, 12345, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        
        mBuilder.setSmallIcon(R.drawable.ic_stat_gcm)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .addAction(R.drawable.ic_launcher, "Yes", pendingIntentYes)
        .addAction(R.drawable.ic_launcher, "Partly", pendingIntentMaybe)
        .addAction(R.drawable.ic_launcher, "No", pendingIntentNo)
        .setContentText(msg) 
        .setContentIntent(contentIntent);
        
     
        

        
       remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_launcher);
        remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.ic_launcher);
 

        remoteViews.setTextViewText(R.id.title,"GCM Notification");
        remoteViews.setTextViewText(R.id.text,msg);
        
      
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
