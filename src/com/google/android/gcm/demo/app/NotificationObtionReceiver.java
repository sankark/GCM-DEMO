/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gcm.demo.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * This {@code WakefulBroadcastReceiver} takes care of creating and managing a
 * partial wake lock for your app. It passes off the work of processing the GCM
 * message to an {@code IntentService}, while ensuring that the device does not
 * go back to sleep in the transition. The {@code IntentService} calls
 * {@code GcmBroadcastReceiver.completeWakefulIntent()} when it is ready to
 * release the wake lock.
 */

public class NotificationObtionReceiver extends BroadcastReceiver {
	private static final String YES_ACTION = "com.google.android.gcm.demo.app.intent.action.YES";
	private static final String MAYBE_ACTION = "com.google.android.gcm.demo.app.intent.action.MAYBE";
	private static final String NO_ACTION = "com.google.android.gcm.demo.app.intent.action.NO";
    @Override
    public void onReceive(Context context, Intent intent) {
    	String action = intent.getAction();

        if(YES_ACTION.equals(action)) {
            Log.v("shuffTest","Pressed YES");
            
            context.startActivity(intent);
        } else if(MAYBE_ACTION.equals(action)) {
            Log.v("shuffTest","Pressed MAYBE");
            context.startActivity(intent);
        } else if(NO_ACTION.equals(action)) {
            Log.v("shuffTest","Pressed NO");
        }
    }
}
