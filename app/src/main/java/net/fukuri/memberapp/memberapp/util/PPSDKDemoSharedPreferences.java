/**
 * Copyright (C) Blogwatcher, inc. All Rights Reserved.
 */
package net.fukuri.memberapp.memberapp.util;

import android.content.Context;

public class PPSDKDemoSharedPreferences extends PPSDKDemoBasePreferences {

    private static final String PPSDKDEMO_BEACON_DETECT_CHECKED_KEY = "ppsdkdemo_beacon_detect_checked";
    private static final String PPSDKDEMO_GEO_AREA_DETECT_CHECKED_KEY = "ppsdkdemo_geo_area_detect_checked";
    private static final String PPSDKDEMO_START_CHECKED_KEY = "ppsdkdemo_start_checked";
    private static final String PPSDKDEMO_OPTIN_KEY = "ppsdkdemo_optin_checked";
    private static final String PPSDKDEMO_SEARCH_BEACON_NAME_KEY = "ppsdkdemo_search_beacon_name";
    private static final String PPSDKDEMO_BEACON_SORT_CHECKED_KEY = "ppsdkdemo_beacon_sort_checked";
    private static final String PPSDKDEMO_WIFI_DETECT_CHECKED_KEY = "ppsdkdemo_wifi_detect_checked";
    private static final String PPSDKDEMO_WIFI_SORT_CHECKED_KEY = "ppsdkdemo_wifi_sort_checked";
    private static final String PPSDKDEMO_SEARCH_WIFI_NAME_KEY = "ppsdkdemo_search_wifi_name";

    /**
     * Beacon Detect.
     */
    public static void setBeaconDetectChecked(final Context context, final boolean isBeaconDetect) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putBoolean(
                PPSDKDEMO_BEACON_DETECT_CHECKED_KEY, isBeaconDetect).commit();
    }

    public static boolean getBeaconDetectChecked(final Context context) {
        if (null == context) {
            return false;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getBoolean(
                PPSDKDEMO_BEACON_DETECT_CHECKED_KEY, true);
    }

    /**
     * GeoArea Detect.
     */
    public static void setGeoAreaDetectChecked(final Context context, final boolean isBeaconDetect) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putBoolean(
                PPSDKDEMO_GEO_AREA_DETECT_CHECKED_KEY, isBeaconDetect).commit();
    }

    public static boolean getGeoAreaDetectChecked(final Context context) {
        if (null == context) {
            return false;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getBoolean(
                PPSDKDEMO_GEO_AREA_DETECT_CHECKED_KEY, true);
    }

    /**
     * PPSDK Started.
     */
    public static void setPPSDKChecked(final Context context, final boolean isGeoAreaDetect) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putBoolean(PPSDKDEMO_START_CHECKED_KEY,
                isGeoAreaDetect).commit();
    }

    public static boolean getPPSDKChecked(final Context context) {
        if (null == context) {
            return false;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getBoolean(PPSDKDEMO_START_CHECKED_KEY,
                true);
    }

    /**
     * OptIn View.
     */
    public static boolean showOptIn(final Context context) {
        if (null == context) {
            return true;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getBoolean(PPSDKDEMO_OPTIN_KEY, true);
    }

    public static void setOptInShown(final Context context, final boolean isOptIn) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putBoolean(PPSDKDEMO_OPTIN_KEY, isOptIn)
                .commit();
    }

    /**
     * Search BeaconName.
     */
    public static String getSearchBeaconName(final Context context) {
        if (null == context) {
            return null;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getString(
                PPSDKDEMO_SEARCH_BEACON_NAME_KEY, null);
    }

    public static void setSearchBeaconName(final Context context, final String searchBeaconName) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putString(
                PPSDKDEMO_SEARCH_BEACON_NAME_KEY, searchBeaconName).commit();
    }

    /**
     * Beacon Name Sort.
     */
    public static void setBeaconSortChecked(final Context context, final boolean isBeaconSort) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putBoolean(
                PPSDKDEMO_BEACON_SORT_CHECKED_KEY, isBeaconSort).commit();
    }

    public static boolean getBeaconSortChecked(final Context context) {
        if (null == context) {
            return false;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getBoolean(
                PPSDKDEMO_BEACON_SORT_CHECKED_KEY, true);
    }

    /**
     * WiFi Detect.
     */
    public static void setWifiDetectChecked(final Context context, final boolean isWifiDetect) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putBoolean(
                PPSDKDEMO_WIFI_DETECT_CHECKED_KEY, isWifiDetect).commit();
    }

    public static boolean getWifiDetectChecked(final Context context) {
        if (null == context) {
            return false;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getBoolean(
                PPSDKDEMO_WIFI_DETECT_CHECKED_KEY, true);
    }

    /**
     * Search WiFiName.
     */
    public static String getSearchWifiName(final Context context) {
        if (null == context) {
            return null;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getString(PPSDKDEMO_SEARCH_WIFI_NAME_KEY,
                null);
    }

    public static void setSearchWifiName(final Context context, final String searchWifiName) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putString(PPSDKDEMO_SEARCH_WIFI_NAME_KEY,
                searchWifiName).commit();
    }

    /**
     * WiFi Name Sort.
     */
    public static void setWifiSortChecked(final Context context, final boolean isWifiSort) {
        if (null == context) {
            return;
        }

        getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).edit().putBoolean(
                PPSDKDEMO_WIFI_SORT_CHECKED_KEY, isWifiSort).commit();
    }

    public static boolean getWifiSortChecked(final Context context) {
        if (null == context) {
            return false;
        }

        return getSharedPreferencesInstance(PPSDKDEMO_PREF_FILE_NAME, context).getBoolean(
                PPSDKDEMO_WIFI_SORT_CHECKED_KEY, true);
    }
}
