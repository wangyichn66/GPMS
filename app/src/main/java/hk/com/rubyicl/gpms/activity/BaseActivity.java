package hk.com.rubyicl.gpms.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import hk.com.rubyicl.gpms.R;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/3 18:49
 *     description:
 *  <pre>
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayout() != 0)
            setContentView(getLayout());
        else {
            LogUtils.e("BaseActivity加载布局错误: 空的布局文件");
            return;
        }
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    /**
     * 获取布局对象 留给子类实现
     */
    protected abstract @LayoutRes
    int getLayout();
}
