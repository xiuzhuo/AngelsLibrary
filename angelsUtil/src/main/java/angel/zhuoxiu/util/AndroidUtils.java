package angel.zhuoxiu.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

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
}
