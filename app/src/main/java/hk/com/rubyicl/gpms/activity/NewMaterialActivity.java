package hk.com.rubyicl.gpms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
import hk.com.rubyicl.gpms.adapter.NewContainsMaterialAdapter;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.RecyclerViewSpacesItemDecoration;
import hk.com.rubyicl.gpms.entity.MaterialEntity;
import hk.com.rubyicl.gpms.entity.SubstanceEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/6 10:54
 *     description: 编辑和新增法规都是这个Activity
 *  <pre>
 */
public class NewMaterialActivity extends BaseActivity {
    @BindView(R.id.name_et)
    EditText name_et;
    @BindView(R.id.barnd_et)
    EditText barnd_et;
    @BindView(R.id.type_et)
    EditText type_et;
    @BindView(R.id.compliance_et)
    EditText compliance_et;
    @BindView(R.id.rv)
    RecyclerView rv;
    //    @BindView(R.id.info_iv)
//    ImageView info_iv;
    private NewContainsMaterialAdapter newContainsMaterialAdapter;
    private MaterialEntity materialEntity;
    public static final int RESULT_CODE_CANCEL = 1;
    public static final int RESULT_CODE_MODIFED = 2;

    @Override
    protected int getLayout() {
        return R.layout.activity_new_material;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getColor(R.color.search_layout_bg), 0);
        StatusBarUtil.setLightMode(this);
        long id;
        try {
            id = Objects.requireNonNull(getIntent().getExtras()).getLong("Id", 0);
        } catch (NullPointerException e) {
            id = 0;
        }
        materialEntity = LitePal.find(MaterialEntity.class, id, true);

        newContainsMaterialAdapter = new NewContainsMaterialAdapter();

        if (materialEntity != null) {
            name_et.setText(materialEntity.getName());
            barnd_et.setText(materialEntity.getBrand());
            type_et.setText(materialEntity.getType());
            compliance_et.setText(materialEntity.getCompliance());
            if (materialEntity.getSubstanceEntityList() != null) {
                newContainsMaterialAdapter.setSubstanceEntities(materialEntity.getSubstanceEntityList());
            }
        }
        newContainsMaterialAdapter.setOnItemLongClickListener(new NewContainsMaterialAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                new AlertDialog.Builder(NewMaterialActivity.this)
                    .setTitle("确认删除")
                    .setMessage("确认删除本行吗？删除后不可撤销！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newContainsMaterialAdapter.MyRemoveItem(position);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewMaterialActivity.this, RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(newContainsMaterialAdapter);

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 0);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 0);
        RecyclerViewSpacesItemDecoration recyclerViewSpacesItemDecoration = new RecyclerViewSpacesItemDecoration(
            NewMaterialActivity.this,
            LinearLayoutManager.VERTICAL,
//            1,
//            getColor(R.color.line_view),
            stringIntegerHashMap);
        rv.addItemDecoration(recyclerViewSpacesItemDecoration);
    }

    private boolean checkForm() {
        if (StringUtils.isEmpty(name_et.getText())) {
            MyUtils.shakeView(name_et);
            ToastUtils.showShort("物料名称不可为空");
            return false;
        }

        List<SubstanceEntity> substanceEntities = newContainsMaterialAdapter.getSubstanceEntities();
//        LogUtils.d(substanceEntities.size());
//        LogUtils.d(rv.getChildCount());
        for (int i = 0; i < substanceEntities.size(); i++) {
            RecyclerView.ViewHolder viewHolder = rv.findViewHolderForLayoutPosition(i);
            if (viewHolder instanceof NewContainsMaterialAdapter.ContentViewHolder) {
                NewContainsMaterialAdapter.ContentViewHolder contentViewHolder = (NewContainsMaterialAdapter.ContentViewHolder) viewHolder;
                if (StringUtils.isEmpty(contentViewHolder.et1.getText())) {
                    MyUtils.shakeView(contentViewHolder.et1);
                    ToastUtils.showLong("含有物资的名称不可为空");
                    rv.scrollToPosition(i);
                    return false;
                }
            }
        }
        return true;
    }

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, NewMaterialActivity.class);
        if (id > 0) {
            intent.putExtra("Id", id);
        }
        context.startActivity(intent);
    }

    public static void startForResult(Activity activity, long id, int request_code) {
        Intent intent = new Intent(activity, NewMaterialActivity.class);
        if (id > 0) {
            intent.putExtra("Id", id);
        }
        activity.startActivityForResult(intent, request_code);
    }

    @OnClick({R.id.back_imgbtn, R.id.save_btn})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_imgbtn:
                setResult(RESULT_CODE_CANCEL);
                NewMaterialActivity.this.finish();
                break;
            case R.id.save_btn:
                if (checkForm()) {
                    List<SubstanceEntity> substanceEntityArrayList = newContainsMaterialAdapter.getSubstanceEntities();
                    if (materialEntity == null)
                        materialEntity = new MaterialEntity();
                    materialEntity.setName(name_et.getText().toString());
                    materialEntity.setBrand(barnd_et.getText().toString());
                    materialEntity.setType(type_et.getText().toString());
                    materialEntity.setCompliance(compliance_et.getText().toString());
                    materialEntity.setTime(TimeUtils.getNowString());
                    materialEntity.getSubstanceEntityList().addAll(substanceEntityArrayList);
                    materialEntity.save();

                    LitePal.saveAll(substanceEntityArrayList);

                    ToastUtils.showShort("保存成功");
                    setResult(RESULT_CODE_MODIFED);
                    NewMaterialActivity.this.finish();
                }
                break;
        }
    }
}
