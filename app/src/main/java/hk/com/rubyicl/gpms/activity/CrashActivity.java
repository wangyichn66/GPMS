package hk.com.rubyicl.gpms.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import hk.com.rubyicl.gpms.R;

/**
 * Created by wangyi on 2019/10/25.
 */
public class CrashActivity extends AppCompatActivity {
    private Unbinder mUnbinder = null;
    private CaocConfig mCaocConfig;
    @BindView(R.id.iv_bug)
    ImageView iv_bug;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCaocConfig = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        if (mCaocConfig == null) {
            finish();
            return;
        }
        StatusBarUtil.setColor(this, getColor(R.color.search_layout_bg), 0);
        StatusBarUtil.setLightMode(this);
        setContentView(R.layout.activity_crash);
        mUnbinder = ButterKnife.bind(this);
        Glide.with(CrashActivity.this)
            .load(R.drawable.crash)
            .into(iv_bug);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    @OnClick({R.id.tv_restart, R.id.tv_log})
    public void onViewClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_restart:
                CustomActivityOnCrash.restartApplication(this, mCaocConfig);
                break;
            case R.id.tv_log:
                new AlertDialog.Builder(CrashActivity.this)
                        .setTitle("崩溃日志")
                        .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent()))
                        .setCancelable(true)
                        .setPositiveButton("关闭", null)
                        .setNeutralButton("复制到剪贴板",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        copyErrorToClipboard();
                                    }
                                })
                        .show();
                break;
        }

    }

    private void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(CrashActivity.this, getIntent());

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //Are there any devices without clipboard...?
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("错误详情", errorInformation);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(CrashActivity.this, "复制完成", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * <pre>
     *     author wangyi
     *     create time: 2020/9/3 下午 05:35
     *     description:
     *  <pre>
     */
    public static class ThreeFragment {
    }
}
