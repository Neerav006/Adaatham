package com.adaatham.suthar.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.view.RegistrationActivity;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {

        public AlarmReceiver() {


        }

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("triggered","alarm.....fast");

            SharedPreferences.Editor editor = context.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE).edit();
            editor.putString(Constants.REG_IN_PROGRESS, "");
            editor.putString("ok", "");
            editor.apply();

            // Your code to execute when the alarm triggers
            // and the broadcast is received.

        }
    }
