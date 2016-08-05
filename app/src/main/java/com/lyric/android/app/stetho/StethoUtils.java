package com.lyric.android.app.stetho;

import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/7 14:03
 */
public class StethoUtils {

    private StethoUtils() {
    }

    public static void initialize(Context context, boolean debug) {
        if (debug) {
            Stetho.initialize(Stetho.newInitializerBuilder(context)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                    .build());
        }
    }
}
