package nl.everlutions.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PrefsUtil {

    private static final String TAG = PrefsUtil.class.getSimpleName();
    private static final String CANT_GET_PREF = "Context is null, cannot read preference: ";
    private static final String CANT_WRITE_PREF = "Context is null, cannot write preference: ";
    private static final String PREF_SETTINGS = "PREF_SETTINGS";

    private static ObjectMapper mObjectMapper;

    static {
        mObjectMapper = new ObjectMapper();
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        mObjectMapper = objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return mObjectMapper;
    }

    public static boolean getBool(Context ctx, String key, boolean defValue) {

        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            return settings.getBoolean(key, defValue);
        } else {
            Log.e(TAG, CANT_GET_PREF + key);
        }

        return defValue;
    }

    static public void setBool(Context ctx, String key, boolean value) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(key, value);

            editor.apply();
        } else {
            Log.e(TAG, CANT_WRITE_PREF + key);
        }
    }

    public static int getInt(Context ctx, String key, int defValue) {

        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            return settings.getInt(key, defValue);
        } else {
            Log.e(TAG, CANT_GET_PREF + key);
        }

        return defValue;
    }

    static public void setInt(Context ctx, String key, int value) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(key, value);
            editor.apply();
        } else {
            Log.e(TAG, CANT_WRITE_PREF + key);
        }
    }

    public static float getFloat(Context ctx, String key, float defValue) {

        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            return settings.getFloat(key, defValue);
        } else {
            Log.e(TAG, CANT_GET_PREF + key);
        }

        return defValue;
    }

    static public void setFloat(Context ctx, String key, float value) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putFloat(key, value);
            editor.apply();
        } else {
            Log.e(TAG, CANT_WRITE_PREF + key);
        }
    }

    public static long getLong(Context ctx, String key, long defValue) {

        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            return settings.getLong(key, defValue);
        } else {
            Log.e(TAG, CANT_GET_PREF + key);
        }

        return defValue;
    }

    static public void setLong(Context ctx, String key, long value) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(key, value);
            editor.apply();
        } else {
            Log.e(TAG, CANT_WRITE_PREF + key);
        }
    }

    public static String getString(Context ctx, String key, String defValue) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            return settings.getString(key, defValue);
        } else {
            Log.e(TAG, CANT_GET_PREF + key);
        }

        return defValue;
    }

    static public void setString(Context ctx, String key, String value) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value);
            editor.apply();
        } else {
            Log.e(TAG, CANT_WRITE_PREF + key);
        }
    }

    public static Object getObject(Context ctx, String key, Class<?> objectClass) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            String jsonObject = settings.getString(key, null);
            if (jsonObject != null) {
                try {
                    return mObjectMapper.readValue(jsonObject, objectClass);
                } catch (IOException e) {
                    Log.e(TAG, "getObject failed " + key + ": " + e.getMessage());
                }
            }
        } else {
            Log.e(TAG, CANT_GET_PREF + key);
        }
        return null;
    }

    public static void setObject(Context ctx, String key, Object object) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            try {
                String value = mObjectMapper.writeValueAsString(object);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(key, value);
                editor.apply();
            } catch (IOException e) {
                Log.e(TAG, "setObject failed: " + e.getMessage());
            }
        } else {
            Log.e(TAG, CANT_WRITE_PREF + key);
        }
    }

    //getObjectList(context, prefkey, new TypeReference<List<BaseObjectDto>>() {});
    public static ArrayList<?> getObjectList(Context ctx, String key, TypeReference<?> objectClass) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            String jsonObject = settings.getString(key, null);
            if (jsonObject != null) {
                try {
                    return mObjectMapper.readValue(jsonObject, objectClass);
                } catch (IOException e) {
                    Log.e(TAG, "getObjectList failed " + key + ": " + e.getMessage());
                }
            }
        } else {
            Log.e(TAG, CANT_GET_PREF + key);
        }
        return null;
    }

    public static void setObjectList(Context ctx, String key, ArrayList<?> objectList) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            try {
                String value = mObjectMapper.writeValueAsString(objectList);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(key, value);
                editor.apply();
            } catch (IOException e) {
                Log.e(TAG, "setObjectList failed: " + e.getMessage());
            }
        } else {
            Log.e(TAG, CANT_WRITE_PREF + key);
        }
    }

    public static void setHashMap(Context ctx, String key, HashMap<?, ?> objectList) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            try {
                String value = mObjectMapper.writeValueAsString(objectList);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(key, value);
                editor.apply();
            } catch (IOException e) {
                Log.e(TAG, "setObjectList failed: " + e.getMessage());
            }
        } else {
            Log.e(TAG, CANT_WRITE_PREF + key);
        }
    }

    public static HashMap<?, ?> getHashMap(Context ctx, String key, Class<?> keyClass, Class<?> valueClass) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                    Context.MODE_PRIVATE);
            String jsonObject = settings.getString(key, null);
            if (jsonObject != null) {
                try {
                    TypeFactory typeFactory = mObjectMapper.getTypeFactory();
                    MapType mapType = typeFactory.constructMapType(HashMap.class, keyClass, valueClass);
                    return mObjectMapper.readValue(jsonObject, mapType);

                } catch (IOException e) {
                    Log.e(TAG, "getHashMap failed " + key + ": " + e.getMessage());
                }
            }
        } else {
            Log.e(TAG, CANT_GET_PREF + key);
        }
        return null;
    }

    /**
     * Clears all the preferences set with this class
     */
    static public void clearAllPreferences(Context ctx) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.apply();
        } else {
            Log.e(TAG, "Content is null, cannot clear preferences");
        }
    }

    /**
     * Clears a single preference
     */
    static public void clearSinglePreference(Context ctx, String preferenceName) {
        if (ctx != null) {
            SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(preferenceName).apply();
        } else {
            Log.e(TAG, CANT_GET_PREF);
        }
    }

    /**
     * @param useDeviceIDifAvailable return the DeviceID as UUID which can only be changed by factory reset, if null
     *                               then UUID is generated
     * @return unique identifier to identify this device/user, generates one if null.
     */
    public static String getUUID(Context ctx, boolean useDeviceIDifAvailable) {
        String deviceID = Utils.getDeviceID(ctx);
        if (deviceID != null && !deviceID.isEmpty() && !deviceID.equalsIgnoreCase("null") && useDeviceIDifAvailable) {
            return deviceID;
        }
        SharedPreferences settings = ctx.getSharedPreferences(PREF_SETTINGS,
                Context.MODE_PRIVATE);
        String uuid = settings.getString("UUID", null);
        if (uuid != null) {
            return uuid;
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("UUID", UUID.randomUUID().toString());
        editor.apply();
        return uuid;
    }

}
