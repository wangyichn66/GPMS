package hk.com.rubyicl.gpms.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import hk.com.rubyicl.gpms.R;

public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.bottom_text)
    TextView bottom_text;

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getColor(R.color.default_activity_bg), 0);
        StatusBarUtil.setLightMode(this);
        bottom_text.getPaint().setFakeBoldText(true);   //兼容小米手机文字加粗的问题
        Animation animation = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.alpha_anim);
        bottom_text.startAnimation(animation);
        startMainActivity();
    }

    private void startMainActivity() {
        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.start(WelcomeActivity.this);
                WelcomeActivity.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(delayTask, 2000);//延时两秒执行 run 里面的操作
    }
}
