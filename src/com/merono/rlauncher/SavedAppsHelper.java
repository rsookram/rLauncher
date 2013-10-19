package com.merono.rlauncher;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class SavedAppsHelper {

	private static final String LOG_TAG = "SavedAppsHelper";
	private static final String APPS_KEY = "apps";

	public static List<AppInfo> getSavedApps(Context context) {
		String appsString = getAppsString(context);
		if (TextUtils.isEmpty(appsString)) {
			Log.d(LOG_TAG, "appsString is empty: " + appsString);
			return null;
		}

        JSONArray array;
        try {
            array = new JSONArray(appsString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        PackageManager pm = context.getPackageManager();
        return SavedAppsHelper.jsonToAppInfo(pm, array);
	}

	public static void saveApps(Context context, List<AppInfo> apps) {
		String appsString = appsToJSON(apps).toString();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString(APPS_KEY, appsString).apply();
	}

	private static String getAppsString(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(APPS_KEY, null);
	}

	private static List<AppInfo> jsonToAppInfo(PackageManager pm,
                                               JSONArray array) {
		List<AppInfo> apps = new LinkedList<AppInfo>();
		for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                AppInfo app = new AppInfo(object, pm, Home.APP_LAUNCH_FLAGS);
                apps.add(app);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
		return apps;
	}

	private static JSONArray appsToJSON(List<AppInfo> apps) {
		JSONArray array = new JSONArray();
		for (AppInfo app : apps) {
			try {
				array.put(app.toJSON());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return array;
	}
}
