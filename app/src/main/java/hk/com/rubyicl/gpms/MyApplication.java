package hk.com.rubyicl.gpms;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.amitshekhar.DebugDB;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.litepal.LitePal;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import hk.com.rubyicl.gpms.activity.CrashActivity;
import hk.com.rubyicl.gpms.activity.MainActivity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/3 下午 04:30
 *     description:
 *  <pre>
 */
public class MyApplication extends Application {
    private static Context mAppContext;
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new MaterialHeader(context);
            }
        });
    }
    public static Context getAppContext() {
        return mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
            .enabled(true)
            .showErrorDetails(true)
            .showRestartButton(true)
            .logErrorOnRestart(false)
            .trackActivities(false)
            .minTimeBetweenCrashesMs(2000)
            .restartActivity(MainActivity.class)
            .errorActivity(CrashActivity.class)
            .apply();

        LitePal.initialize(this);

        DebugDB.getAddressLog();
    }

}
