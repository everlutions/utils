package nl.everlutions.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;


@SuppressLint("SimpleDateFormat")
public class Utils {

    public static final String TAG = Utils.class.getSimpleName();


    public static int getAndroiAPIVersion() {
        return VERSION.SDK_INT;
    }

    public static String getAppVersionName(Context ctx) {
        String versionName = "Error getting version name";
        try {
            versionName = ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String encodeToUTF8(String url) {
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            Log.e(TAG, "fileName encode error: " + url);
        }
        return encodedString;
    }

    /**
     * @param nameOfJsonFile without the .json extension http://www.objgen.com/json?demo=true
     */
    public static Object getObjectFromJsonFile(Context context, String nameOfJsonFile, Class<?> clazz) {
        String json = loadJSONFromAsset(context, nameOfJsonFile);
        return deserialize(json, clazz);
    }

    /**
     * @param nameOfJsonFile without the .json extension example: (context, prefkey, new TypeReference<List<BaseObjectDto>>()
     *                       {});
     *
     *                       http://www.objgen.com/json?demo=true
     */
    public static ArrayList<?> getObjectArrayFromJsonFile(Context context, String nameOfJsonFile,
            TypeReference<?> clazz) {
        String json = loadJSONFromAsset(context, nameOfJsonFile);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serialize(Object value, Class<?> clazz) {
        if (value == null) {
            return null;
        }
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static <T> T deserialize(String value, Class<T> clazz) {
        if (value == null) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(value, clazz);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static String loadJSONFromAsset(Context ctx, String fileName) {
        String json = null;
        try {

            InputStream is = ctx.getAssets().open(fileName + ".json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static Calendar parseFormatAndDateStringToCal(String dateString, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            Log.e(TAG, "dateParse error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * @param min is included
     * @param max is included
     * @return example min=0, max=2 can return 3 values [0,1,2]
     */
    public static int getRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static Uri makeGeoURI(double lat, double lon, String label) {
        // String q = lat + "," + lon + " (" + label + ")";
        // return Uri.parse("geo:" + lat + "," + lon + "?q=" + Uri.encode(q) + "&z=16");
        return Uri.parse("http://maps.google.com/maps?q="
                + Uri.encode("loc:" + lat + "," + lon + " (" + label + ")"));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String formatImageUrl(String url, int w, int h) {
        return url.replace("[format]", w + "x" + h);
    }

    /**
     * Formats a price as display currency
     *
     * @param zeroesToDash replace ,00 with ,-
     */
    public static String formatPrice(double price, boolean zeroesToDash) {
        String priceString = formatDecimal(price);
        priceString = zeroesToDash ? priceString + ",-" : priceString + ",00";
        return "\u20AC\u00A0" + priceString;
    }

    public static String formatPrice(double price, boolean zeroesToDash, boolean withDecimals) {
        price = round(price, 2);
        String priceString = formatDecimal(price);
        if (withDecimals) {
            priceString = zeroesToDash ? priceString.replace(",00", ",-") : priceString;
        } else {
            priceString = String.format(new Locale("nl", "NL"), "%.0f", price);
            priceString = zeroesToDash ? priceString + ",-" : priceString + ",00";
        }

        return "\u20AC\u00A0" + priceString;
    }

    public static String getUserAgentString(Context context, String appName) {
        String version = "?";
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            version = "" + manager.versionCode;
        } catch (NameNotFoundException e) {
            // Handle exception
        }

        return appName + "/" + version + " Android (" + VERSION.RELEASE
                + "; API " + VERSION.SDK_INT + ")";
    }

    public static int getApplicationVersionCode(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }

    }

    public static String minutesToDisplayText(int notifyMinutesBefore) {

        StringBuilder b = new StringBuilder();
        if (notifyMinutesBefore >= 24 * 60) {
            int d = notifyMinutesBefore / (24 * 60);
            b.append(Integer.toString(d));
            b.append(d > 1 ? " dagen" : " dag");
            notifyMinutesBefore %= 24 * 60;
            if (notifyMinutesBefore > 0) {
                b.append(", ");
            }
        }
        if (notifyMinutesBefore >= 60) {
            b.append((notifyMinutesBefore / 60) + " uur");
            notifyMinutesBefore %= 60;
            if (notifyMinutesBefore > 0) {
                b.append(", ");
            }
        }
        if (notifyMinutesBefore > 0) {
            b.append(notifyMinutesBefore + " minuten");
        }
        return b.toString();
    }


    public static boolean isEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    public static void expandView(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density) * 3);
        v.startAnimation(a);
    }

    public static void collapseView(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 3);
        v.startAnimation(a);
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        boolean connected = getNetworkInfo(context) != null
                && info.isAvailable()
                && info.isConnected();
        return connected;
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static void lockOrientation(Activity activity) {
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int tempOrientation = activity.getResources().getConfiguration().orientation;
        int orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        switch (tempOrientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270) {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                } else {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                }
        }
        activity.setRequestedOrientation(orientation);
    }


    public static String formatDecimal(double bigNumber) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("nl", "NL"));
        return nf.format(bigNumber);
    }

    public static double round(double dvalue, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(dvalue);
        bd = bd.setScale(places, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    /**
     * @param directory if null Environment.DIRECTORY_PICTURES is used
     */
    public static File createImageFile(@Nullable File directory) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = directory == null ? Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) : directory;
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // image.getAbsolutePath();
        return image;
    }
}
