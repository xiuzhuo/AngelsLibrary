package angel.zhuoxiu.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zxui on 27/04/15.
 */
public class AndroidUtils {

    /**
     * Check if a text is email form.
     *
     * @param email Text to check
     * @return check result
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Test if a text contains only number
     *
     * @param str Text to check
     * @return check result
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public static String parseFileByUri(Activity activity, Intent data) {
        String filePath = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri u = data.getData();
            //noinspection ResourceType
            activity.getContentResolver().takePersistableUriPermission(u, flags);
            String id = u.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA};
            final String imageOrderBy = null;
            Uri u1 = null;
            String state = Environment.getExternalStorageState();
            if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
                u1 = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
            else
                u1 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor c = activity.managedQuery(u1, imageColumns, MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);
            if (c.moveToFirst()) {
                filePath = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));  //filePath represents string variable to hold the path of image
            }
        } else {
            Uri imgUri = data.getData();
            Cursor c1 = activity.getContentResolver().query(imgUri, null, null, null, null);
            if (c1 == null) {
                filePath = imgUri.getPath();  //filePath represents string variable to hold the path of image
            } else {
                c1.moveToFirst();
                int idx = c1.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                filePath = c1.getString(idx);  //filePath represents string variable to hold the path of image
                c1.close();
            }
        }
        return filePath;
    }


    /**
     * Judge if it is Tablet
     *
     * @return
     */
    public static boolean isTablet(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.2) {
            return true;
        }
        return false;
    }

    public static boolean isInternetConnected(Context context) {
        return isMobileConnected(context) || isWifiConnected(context);
    }

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // mobile 3G Data Network
        if (connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null) {
            return false;
        }

        NetworkInfo.State mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        return (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING);
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        return (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING);
    }

    public static void showHideKeyboard(Activity activity, boolean show) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            if (show) {
                imm.showSoftInput(view, 0);
            } else {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void toggleKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
//
//    public static CharSequence formatTimeStampString(Context context, long when, boolean fullFormat) {
//        Time then = new Time();
//        then.set(when);
//        Time now = new Time();
//        now.setToNow();
//        if (true){
//            String result=DateUtils.getRelativeDateTimeString(context, when, DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0).toString();
//
//            return result.split(",")[0];
//        }
//
//        // Basic settings for formatDateTime() we want for all cases.
//        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT |
//                DateUtils.FORMAT_ABBREV_ALL |
//                DateUtils.FORMAT_CAP_AMPM;
//        // If the message is from a different year, show the date and year.
//        if (then.year != now.year) {
//            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
//        } else if (then.yearDay != now.yearDay) {
//            // If it is from a different day than today, show only the date.
//            format_flags |= DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME;
//        } else {
//            // Otherwise, if the message is from today, show the time.
//            format_flags |= DateUtils.FORMAT_SHOW_TIME;
//            format_flags = DateUtils.FORMAT_ABBREV_RELATIVE;
//
////            return DateUtils.getRelativeDateTimeString(context, when, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);
//            return DateUtils.getRelativeTimeSpanString(when,System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
//        }
//        // If the caller has asked for full details, make sure to show the date
//        // and time no matter what we've determined above (but still make showing
//        // the year only happen if it is a different year from today).
//        if (fullFormat) {
//            format_flags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
//        }
//        return DateUtils.formatDateTime(context, when, format_flags);
//    }


    public static void forceLocale(Context context, Locale locale) {
        Configuration conf = context.getApplicationContext().getResources().getConfiguration();
        conf.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(conf, context.getApplicationContext().getResources().getDisplayMetrics());
        Configuration systemConf = Resources.getSystem().getConfiguration();
        systemConf.locale = locale;
        Resources.getSystem().updateConfiguration(systemConf, Resources.getSystem().getDisplayMetrics());
        Locale.setDefault(locale);
    }

    public static void copyText(Context context, String content) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    public static String pasteText(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getPrimaryClip() != null && cmb.getPrimaryClip().getItemCount() > 0 ? cmb.getPrimaryClip().getItemAt(0).getText().toString().trim() : null;
    }
}
