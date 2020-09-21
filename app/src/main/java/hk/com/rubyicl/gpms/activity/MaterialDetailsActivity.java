package hk.com.rubyicl.gpms.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import hk.com.rubyicl.gpms.ContainsMaterialAdapter;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.RecyclerViewSpacesItemDecoration;
import hk.com.rubyicl.gpms.entity.MaterialEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/5 20:14
 *     description:
 *  <pre>
 */
public class MaterialDetailsActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.barnd_tv)
    TextView barnd_tv;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.compliance_tv)
    TextView compliance_tv;

    private long id;
    private MaterialEntity materialEntity;
    private ContainsMaterialAdapter containsMaterialAdapter;
    private final int REQUEST_CODE = 100;

    @Override
    protected int getLayout() {
        return R.layout.activity_material_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getColor(R.color.search_layout_bg), 0);
        StatusBarUtil.setLightMode(this);
        id = Objects.requireNonNull(getIntent().getExtras()).getLong("Id");
        materialEntity = LitePal.find(MaterialEntity.class, id, true);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == NewMaterialActivity.RESULT_CODE_MODIFED) {
            materialEntity = LitePal.find(MaterialEntity.class, id, true);
            containsMaterialAdapter.setSubstanceEntities(materialEntity.getSubstanceEntityList());
        }
    }

    private void initView() {
        LogUtils.d(materialEntity);
        name.setText(materialEntity.getName());
        barnd_tv.setText(materialEntity.getBrand());
        type.setText(materialEntity.getType());
        time_tv.setText(materialEntity.getTime());
        compliance_tv.setText(materialEntity.getCompliance());
        containsMaterialAdapter = new ContainsMaterialAdapter();
        containsMaterialAdapter.setSubstanceEntities(materialEntity.getSubstanceEntityList());
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 0);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 0);
        RecyclerViewSpacesItemDecoration recyclerViewSpacesItemDecoration = new RecyclerViewSpacesItemDecoration(
            MaterialDetailsActivity.this,
            LinearLayoutManager.VERTICAL,
//            1,
//            getColor(R.color.line_view),
            stringIntegerHashMap);
        rv.addItemDecoration(recyclerViewSpacesItemDecoration);
        rv.setLayoutManager(new LinearLayoutManager(MaterialDetailsActivity.this, RecyclerView.VERTICAL, false));
        rv.setAdapter(containsMaterialAdapter);
    }

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, MaterialDetailsActivity.class);
        intent.putExtra("Id", id);
        context.startActivity(intent);
    }


    @OnClick({R.id.back_imgbtn, R.id.modfiy_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_imgbtn:
                MaterialDetailsActivity.this.finish();
                break;
            case R.id.modfiy_btn:
                NewMaterialActivity.startForResult(MaterialDetailsActivity.this, id, REQUEST_CODE);
                break;
        }
    }

}
