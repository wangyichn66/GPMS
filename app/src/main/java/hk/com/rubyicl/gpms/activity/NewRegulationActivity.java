package hk.com.rubyicl.gpms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import hk.com.rubyicl.gpms.MyUtils;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.RecyclerViewSpacesItemDecoration;
import hk.com.rubyicl.gpms.adapter.NewContainsMaterialAdapter;
import hk.com.rubyicl.gpms.adapter.NewRequlationItemAdapter;
import hk.com.rubyicl.gpms.entity.MaterialEntity;
import hk.com.rubyicl.gpms.entity.RegulationEntity;
import hk.com.rubyicl.gpms.entity.RegulationItemEntity;
import hk.com.rubyicl.gpms.entity.SubstanceEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/25 9:51
 *     description:
 *  <pre>
 */
public class NewRegulationActivity extends BaseActivity {
    @BindView(R.id.name_et)
    EditText name_et;
    @BindView(R.id.remark_et)
    EditText remark_et;
    @BindView(R.id.time_et)
    EditText time_et;
    @BindView(R.id.rv)
    RecyclerView rv;
    public static final int RESULT_CODE_CANCEL = 1;
    public static final int RESULT_CODE_MODIFED = 2;
    private RegulationEntity regulationEntity;
    private NewRequlationItemAdapter newRequlationItemAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_new_regulation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getColor(R.color.search_layout_bg), 0);
        StatusBarUtil.setLightMode(this);
        long id;
        try {
            id = Objects.requireNonNull(getIntent().getExtras()).getLong("Id", 0);
        } catch (NullPointerException e) {
            id = 0;
        }
        regulationEntity = LitePal.find(RegulationEntity.class, id, true);
        newRequlationItemAdapter = new NewRequlationItemAdapter();
        if (regulationEntity != null) {
            name_et.setText(regulationEntity.getName());
            remark_et.setText(regulationEntity.getRemarks());
            if (regulationEntity.getRegulationItemEntityList() != null) {
                newRequlationItemAdapter.setRegulationItemEntities(regulationEntity.getRegulationItemEntityList());
            }
        }
        newRequlationItemAdapter.setOnItemLongClickListener(new NewRequlationItemAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                new AlertDialog.Builder(NewRegulationActivity.this)
                    .setTitle("确认删除")
                    .setMessage("确认删除本行吗？删除后不可撤销！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newRequlationItemAdapter.MyRemoveItem(position);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewRegulationActivity.this, RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(newRequlationItemAdapter);

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 0);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 0);
        RecyclerViewSpacesItemDecoration recyclerViewSpacesItemDecoration = new RecyclerViewSpacesItemDecoration(
            NewRegulationActivity.this,
            LinearLayoutManager.VERTICAL,
//            1,
//            getColor(R.color.line_view),
            stringIntegerHashMap);
        rv.addItemDecoration(recyclerViewSpacesItemDecoration);
    }


    public static void start(Context context, long id) {
        Intent intent = new Intent(context, NewRegulationActivity.class);
        if (id > 0) {
            intent.putExtra("Id", id);
        }
        context.startActivity(intent);
    }

    public static void startForResult(Activity activity, long id, int request_code) {
        Intent intent = new Intent(activity, NewRegulationActivity.class);
        if (id > 0) {
            intent.putExtra("Id", id);
        }
        activity.startActivityForResult(intent, request_code);
    }

    private boolean checkForm() {
        if (StringUtils.isEmpty(name_et.getText())) {
            MyUtils.shakeView(name_et);
            ToastUtils.showShort("物料名称不可为空");
            return false;
        }

        List<RegulationItemEntity> regulationItemEntityList = newRequlationItemAdapter.getRegulationItemEntities();
//        LogUtils.d(substanceEntities.size());
//        LogUtils.d(rv.getChildCount());
        for (int i = 0; i < regulationItemEntityList.size(); i++) {
            RecyclerView.ViewHolder viewHolder = rv.findViewHolderForLayoutPosition(i);
            if (viewHolder instanceof NewRequlationItemAdapter.ContentViewHolder) {
                NewRequlationItemAdapter.ContentViewHolder contentViewHolder = (NewRequlationItemAdapter.ContentViewHolder) viewHolder;
                if (StringUtils.isEmpty(contentViewHolder.et1.getText())) {
                    MyUtils.shakeView(contentViewHolder.et1);
                    ToastUtils.showLong("法规含有物资的 物质名称 不可为空");
                    rv.scrollToPosition(i);
                    return false;
                }
                if (StringUtils.isEmpty(contentViewHolder.et2.getText())) {
                    MyUtils.shakeView(contentViewHolder.et2);
                    ToastUtils.showLong("法规含有物资的 Substance Name 不可为空");
                    rv.scrollToPosition(i);
                    return false;
                }
                if (StringUtils.isEmpty(contentViewHolder.et3.getText())) {
                    MyUtils.shakeView(contentViewHolder.et3);
                    ToastUtils.showLong("法规含有物资的 CAS NO. 不可为空");
                    rv.scrollToPosition(i);
                    return false;
                }
                if (StringUtils.isEmpty(contentViewHolder.et4.getText())) {
                    MyUtils.shakeView(contentViewHolder.et4);
                    ToastUtils.showLong("法规含有物资的 限制要求 不可为空");
                    rv.scrollToPosition(i);
                    return false;
                }
            }
        }
        return true;
    }

    @OnClick({R.id.back_imgbtn, R.id.save_btn})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_imgbtn:
                setResult(RESULT_CODE_CANCEL);
                NewRegulationActivity.this.finish();
                break;
            case R.id.save_btn:
                if (checkForm()) {
                    List<RegulationItemEntity> regulationItemEntityList = newRequlationItemAdapter.getRegulationItemEntities();
                    if (regulationEntity == null)
                        regulationEntity = new RegulationEntity();
                    regulationEntity.setName(name_et.getText().toString());
                    regulationEntity.setRemarks(remark_et.getText().toString());
                    regulationEntity.setTime(time_et.getText().toString());
//                    materialEntity.setTime(TimeUtils.getNowString());
                    regulationEntity.getRegulationItemEntityList().addAll(regulationItemEntityList);
                    regulationEntity.save();

                    LitePal.saveAll(regulationItemEntityList);

                    ToastUtils.showShort("保存成功");
                    setResult(RESULT_CODE_MODIFED);
                    NewRegulationActivity.this.finish();
                }
                break;
        }
    }
}
