package com.merono.rlauncher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

public class SavedAppsHelper {

	private static final String LOG_TAG = "SavedAppsHelper";
	private static final String FAVORITES_PATH = "favourites.json";

	public static List<AppInfo> getSavedApps(Context context) {
		String appsString = getAppsString(context);
		if (TextUtils.isEmpty(appsString)) {
			Log.e(LOG_TAG, "appsString is empty: " + appsString);
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

	private static String getAppsString(Context context) {
		InputStream is;
        try {
            is = context.getAssets().open(FAVORITES_PATH);
            return SavedAppsHelper.convertStreamToString(is);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't find or open favourites file", e);
            return null;
        }
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

    private static String convertStreamToString(InputStream is)
            throws IOException {
        StringWriter writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }
}
