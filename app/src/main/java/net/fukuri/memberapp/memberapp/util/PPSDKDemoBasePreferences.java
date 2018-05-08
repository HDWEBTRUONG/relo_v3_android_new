/**
 * Copyright (C) Blogwatcher, inc. All Rights Reserved.
 */
package net.fukuri.memberapp.memberapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PPSDKDemoBasePreferences {

    protected static final String PPSDKDEMO_PREF_FILE_NAME = "ppsdkdemo_pref_data";

    protected static SharedPreferences getSharedPreferencesInstance(final String fileName, final Context context) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
}
