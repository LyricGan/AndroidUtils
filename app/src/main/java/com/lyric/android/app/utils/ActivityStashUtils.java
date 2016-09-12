package com.lyric.android.app.utils;

import android.app.Activity;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author lyricgan
 * @description
 * @time 2016/8/19 14:58
 */
public class ActivityStashUtils {
    private static final Map<String, List<ActivityInfo>> COUNTER = new HashMap<String, List<ActivityInfo>>();
    private static WeakReference<Activity> mCurrentActivity;
    private static LinkedList<String> mActivityList = new LinkedList<>();

    public static void onCreated(Activity activity) {
        String activityName = activity.getClass().getName();
        List<ActivityInfo> activityInfoList = COUNTER.get(activityName);
        if (activityInfoList == null) {
            activityInfoList = new ArrayList<ActivityInfo>();
            COUNTER.put(activityName, activityInfoList);
        }
        activityInfoList.add(new ActivityInfo(activity, mActivityList.pollLast()));
        mActivityList.add(activityName);
    }

    public static void onDestroy(Activity activity) {
        String activityName = activity.getClass().getName();
        List<ActivityInfo> activityInfoList = COUNTER.get(activityName);
        if (activityInfoList != null) {
            Iterator<ActivityInfo> iterator = activityInfoList.iterator();
            while (iterator.hasNext()) {
                ActivityInfo item = iterator.next();
                if (item.referenceActivity.get() != null && item.referenceActivity.get() == activity) {
                    iterator.remove();
                    break;
                }
            }
        }
        if (mCurrentActivity != null && mCurrentActivity.get() == activity) {
            setCurrentActivity(null);
        }
    }

    public static void finishActivity(String activityName) {
        List<ActivityInfo> activities = COUNTER.get(activityName);
        if (activities != null && !activities.isEmpty()) {
            for (ActivityInfo info : activities) {
                if (info.referenceActivity != null && info.referenceActivity.get() != null) {
                    info.referenceActivity.get().finish();
                }
            }
            activities.clear();
        }
    }

    public static void finishActivity(Activity activity) {
        String activityName = activity.getClass().getName();
        List<ActivityInfo> activities = COUNTER.get(activityName);
        if (activities != null && !activities.isEmpty()) {
            for (ActivityInfo info : activities) {
                if (info != null && info.referenceActivity.get() != null) {
                    info.referenceActivity.get().finish();
                }
            }
            activities.clear();
        }
    }

    public static void finishAll() {
        for (String key : COUNTER.keySet()) {
            finishActivity(key);
        }
        COUNTER.clear();
        mActivityList.clear();
    }

    public static boolean hasActivityInStack(String activityName) {
        List<ActivityInfo> activities = COUNTER.get(activityName);
        return activities != null && !activities.isEmpty();
    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity != null ? mCurrentActivity.get() : null;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        ActivityStashUtils.mCurrentActivity = new WeakReference<Activity>(currentActivity);
    }

    public static List<Activity> getActivity(String activityName) {
        List<ActivityInfo> activityInfoList = COUNTER.get(activityName);
        List<Activity> activityList;
        if (activityInfoList != null && !activityInfoList.isEmpty()) {
            activityList = new ArrayList<>();
            for (ActivityInfo info : activityInfoList) {
                if (info != null && info.referenceActivity.get() != null) {
                    activityList.add(info.referenceActivity.get());
                }
            }
            return activityList;
        }
        return null;
    }

    public static ActivityInfo getLastActivityInfo(String currentActivityName) {
        List<ActivityInfo> activityInfoList = COUNTER.get(currentActivityName);
        if (activityInfoList == null || activityInfoList.size() == 0) {
            return null;
        }
        final ActivityInfo activityInfo = activityInfoList.get(activityInfoList.size() - 1);
        if (activityInfo.lastActivityName == null) {
            return null;
        }
        activityInfoList = COUNTER.get(activityInfo.lastActivityName);
        if (activityInfoList == null || activityInfoList.size() == 0) {
            return null;
        }
        return activityInfoList.get(activityInfoList.size() - 1);
    }

    public static Activity getLastActivity(String activityName) {
        int index = mActivityList.lastIndexOf(activityName);
        if (index > 0) {
            String previousActivity = mActivityList.get(index - 1);
            List<Activity> activity = getActivity(previousActivity);
            if (activity != null && !activity.isEmpty()) {
                return activity.get(activity.size() - 1);
            }
        }
        return null;
    }

    public static class ActivityInfo {
        public WeakReference<Activity> referenceActivity;
        public Bundle data;
        public Class<? extends Activity> clazz;
        public String lastActivityName;

        ActivityInfo(Activity activity, String lastActivityName) {
            this.referenceActivity = new WeakReference<Activity>(activity);
            this.data = activity.getIntent().getExtras();
            this.clazz = activity.getClass();
            this.lastActivityName = lastActivityName;
        }
    }
}
