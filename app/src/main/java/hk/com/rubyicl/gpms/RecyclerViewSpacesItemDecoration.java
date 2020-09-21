package hk.com.rubyicl.gpms;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.HashMap;

/**
 * Created by wangyi on 2020/6/4.
 * 参考资料
 * https://www.jianshu.com/p/e68a0b5fd383
 * 这里有所改动 目前只适合竖向布局 而且我加了FootView
 */
public class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private HashMap<String, Integer> mSpaceValueMap;

    public static final String TOP_DECORATION = "top_decoration";
    public static final String BOTTOM_DECORATION = "bottom_decoration";
    public static final String LEFT_DECORATION = "left_decoration";
    public static final String RIGHT_DECORATION = "right_decoration";

    private Drawable mDivider;
    private Paint mPaint;
    private int mDividerHeight = 0;//分割线高度，默认为1px
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};//使用系统自带的listDivider

    //暂不使用这个属性 默认竖直显示好了
    private int mOrientation = LinearLayoutManager.VERTICAL;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL


    private RecyclerViewSpacesItemDecoration(Context context, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mOrientation = orientation;

        final TypedArray a = context.obtainStyledAttributes(ATTRS);//使用TypeArray加载该系统资源
        mDivider = a.getDrawable(0);
        a.recycle();//缓存
    }

    /**
     * @param context
     * @param orientation 列表方向
     */
    public RecyclerViewSpacesItemDecoration(Context context, int orientation, @NonNull HashMap<String, Integer> mSpaceValueMap) {
        this(context, orientation);
        this.mSpaceValueMap = mSpaceValueMap;
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation      列表方向
     * @param dividerHeight_dp 分割线高度
     * @param dividerColor     分割线颜色
     */

    @Deprecated
    public RecyclerViewSpacesItemDecoration(Context context, int orientation, int dividerHeight_dp, int dividerColor) {
        this(context, orientation);
        mDividerHeight = ConvertUtils.dp2px(dividerHeight_dp);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Deprecated
    public RecyclerViewSpacesItemDecoration(Context context, int orientation, int dividerHeight_dp, int dividerColor, @NonNull HashMap<String, Integer> mSpaceValueMap) {
        this(context, orientation, dividerHeight_dp, dividerColor);
        this.mSpaceValueMap = mSpaceValueMap;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mDividerHeight == 0) return;
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();//获取分割线的左边距，即RecyclerView的padding值
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();//分割线右边距
        final int childSize = parent.getChildCount() - 1;
        //遍历所有item view，为它们的下方绘制分割线
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //绘制纵向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount() - 1;
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mSpaceValueMap == null) return;
        if (mSpaceValueMap.get(TOP_DECORATION) != null)
            outRect.top = ConvertUtils.dp2px(mSpaceValueMap.get(TOP_DECORATION));
        if (mSpaceValueMap.get(LEFT_DECORATION) != null)
            outRect.left = ConvertUtils.dp2px(mSpaceValueMap.get(LEFT_DECORATION));
        if (mSpaceValueMap.get(RIGHT_DECORATION) != null)
            outRect.right = ConvertUtils.dp2px(mSpaceValueMap.get(RIGHT_DECORATION));
        if (mSpaceValueMap.get(BOTTOM_DECORATION) != null)
            outRect.bottom = ConvertUtils.dp2px(mSpaceValueMap.get(BOTTOM_DECORATION)) + mDividerHeight;

//        int pos = parent.getChildLayoutPosition(view);
//        int itemCount = state.getItemCount() - 1;
//        if (pos == 0) {
//            outRect.top = mSpaceValueMap.get(TOP_DECORATION) * 2;
////            outRect.top = 0;
//        } else if (pos == itemCount) {
//            outRect.bottom = mSpaceValueMap.get(BOTTOM_DECORATION) * 2;
////            outRect.bottom = 0;
//        }


    }
}
