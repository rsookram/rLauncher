/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.example.android.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

/** Represents a launchable application. */
public class ApplicationInfo {

    // Keys used in the JSON representation of this class
    private static final String KEY_PACKAGE = "package";
    private static final String KEY_CLASS = "class";

    public Intent intent;

    public Drawable icon;

    public ApplicationInfo() {
    }

    public ApplicationInfo(JSONObject obj, PackageManager manager, int launchFlags) throws JSONException {
        String packageName = obj.getString(KEY_PACKAGE);
        String className = obj.getString(KEY_CLASS);

        ComponentName cn = new ComponentName(packageName, className);
        setActivity(cn, launchFlags);

        ResolveInfo resolveInfo = manager.resolveActivity(intent, 0);
        icon = resolveInfo.activityInfo.loadIcon(manager);
    }

    /**
     * Creates the application intent based on a component name and various
     * launch flags.
     *
     * @param className   the class name of the component representing the intent
     * @param launchFlags the launch flags
     */
    final void setActivity(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationInfo)) {
            return false;
        }

        ApplicationInfo that = (ApplicationInfo) o;
        return intent.getComponent().getClassName()
                .equals(that.intent.getComponent().getClassName());
    }

    @Override
    public int hashCode() {
        final String name = intent.getComponent().getClassName();
        return ((name != null) ? name.hashCode() : 0);
    }
}
