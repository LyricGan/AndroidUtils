package com.lyric.android.library.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author lyric
 * @description
 * @time 2016/6/20 14:23
 */
public class JsonHelper {
    private final JsonObjectHelper mHelper = new JsonObjectHelper();

    public static JsonObjectHelper newJsonObjectHelper() {
        return new JsonObjectHelper();
    }

    public static JsonArrayHelper newJsonArrayHelper() {
        return new JsonArrayHelper();
    }

    public JsonHelper put(String key, Object value) {
        if (value instanceof JsonObjectHelper) {
            mHelper.put(key, ((JsonObjectHelper) value).toJSONObject());
        } else if (value instanceof JsonArrayHelper) {
            mHelper.put(key, ((JsonArrayHelper) value).toJSONArray());
        } else {
            mHelper.put(key, value);
        }
        return this;
    }

    public JSONObject toJSONObject() {
        return mHelper.toJSONObject();
    }

    public String toJson() {
        return mHelper.toString();
    }

    public static class JsonArrayHelper {
        private final JSONArray jsonArray = new JSONArray();

        public JsonArrayHelper put(Object value) {
            jsonArray.put(value);
            return this;
        }

        public JsonArrayHelper put(int index, Object value) {
            try {
                jsonArray.put(index, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public JSONArray toJSONArray() {
            return jsonArray;
        }

        @Override
        public String toString() {
            return jsonArray.toString();
        }
    }

    public static class JsonObjectHelper {
        private final JSONObject jsonObject = new JSONObject();

        public JsonObjectHelper put(String key, Object value) {
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public JSONObject toJSONObject() {
            return jsonObject;
        }

        @Override
        public String toString() {
            return jsonObject.toString();
        }
    }
}
