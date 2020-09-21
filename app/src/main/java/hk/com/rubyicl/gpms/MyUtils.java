package hk.com.rubyicl.gpms;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/9 上午 11:52
 *     description:
 *  <pre>
 */
public class MyUtils {
    public static void shakeView(@NonNull View view) {
        Animation shake = AnimationUtils.loadAnimation(MyApplication.getAppContext(), R.anim.shake);
        CycleInterpolator interpolator = new CycleInterpolator(7);
        shake.setInterpolator(interpolator);
        view.startAnimation(shake);
    }

    public static String getDebugDBAddressLog(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                return (String) value;
            } catch (Exception ignore) {

            }
        }
        return "";
    }
}
