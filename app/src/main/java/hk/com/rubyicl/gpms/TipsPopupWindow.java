package hk.com.rubyicl.gpms;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;

import razerdp.basepopup.BaseLazyPopupWindow;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/11 下午 04:53
 *     description: 为什么用 BaseLazyPopupWindow 而不是 BasePopupWindow 参见下面这个链接
 *     https://www.yuque.com/razerdp/basepopup/thgnsi
 * @deprecated not cool
 *  <pre>
 */
@Deprecated
public class TipsPopupWindow extends BaseLazyPopupWindow {
    TextView tips_tv;
    private Context context;

    public TipsPopupWindow(Context context) {
        super(context);
        this.context = context;
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.tips_layout);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        tips_tv = contentView.findViewById(R.id.tips_tv);
        tips_tv.setText(context.getString(R.string.tips, MyUtils.getDebugDBAddressLog(context) +"\n将手机和电脑连接到同一局域网或者wifi\n然后输入上面这个地址就可以调试数据库"));
    }


}
