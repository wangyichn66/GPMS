package hk.com.rubyicl.gpms.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.RecyclerViewSpacesItemDecoration;
import hk.com.rubyicl.gpms.adapter.RegulationItemAdapter;
import hk.com.rubyicl.gpms.entity.RegulationEntity;
import hk.com.rubyicl.gpms.entity.RegulationItemEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/22 10:26
 *     description:
 *  <pre>
 */
public class RegulationDetailsActivity extends BaseActivity {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.remark_tv)
    TextView remark_tv;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.search_btn)
    Button search_btn;
    @BindView(R.id.search_autotv)
    AutoCompleteTextView search_autotv;
    @BindView(R.id.delete_imgbtn)
    ImageButton delete_imgbtn;
    private long id;
    private RegulationEntity regulationEntity;
    private RegulationItemAdapter regulationItemAdapter;
    private final int REQUEST_CODE = 100;

    @Override
    protected int getLayout() {
        return R.layout.activity_regulation_details;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getColor(R.color.search_layout_bg), 0);
        StatusBarUtil.setLightMode(this);
        id = Objects.requireNonNull(getIntent().getExtras()).getLong("Id");
        regulationEntity = LitePal.find(RegulationEntity.class, id, true);
        initView();
        loadAllData();
    }

    private void initView() {
        LogUtils.d(regulationEntity);
        regulationItemAdapter = new RegulationItemAdapter();
        name.setText(regulationEntity.getName());
        remark_tv.setText(regulationEntity.getRemarks());
        time_tv.setText(regulationEntity.getTime());
        search_autotv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    delete_imgbtn.setVisibility(View.VISIBLE);
                } else if (count == 0) {
                    delete_imgbtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filtrationData(String like) {
        String like_str = "%" + like + "%";
        List<RegulationItemEntity> regulationItemEntityList =
            LitePal.where("(substances_name_cn like ? or substances_name_eg like ? " +
                "or CAS_No like ? or threshold like ?) and regulationentity_id = ?", like_str, like_str, like_str, like_str, String.valueOf(id)).find(RegulationItemEntity.class);
        regulationItemAdapter.setData(regulationItemEntityList);
        ToastUtils.showLong("查找到%d条数据", regulationItemEntityList.size());
    }

    private void loadAllData() {
        List<RegulationItemEntity> regulationItemEntityList = regulationEntity.getRegulationItemEntityList();
        regulationItemAdapter.setData(regulationItemEntityList);
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 0);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 0);
        RecyclerViewSpacesItemDecoration recyclerViewSpacesItemDecoration = new RecyclerViewSpacesItemDecoration(
            RegulationDetailsActivity.this,
            LinearLayoutManager.VERTICAL,
//            1,
//            getColor(R.color.line_view),
            stringIntegerHashMap);
        rv.addItemDecoration(recyclerViewSpacesItemDecoration);
        rv.setLayoutManager(new LinearLayoutManager(RegulationDetailsActivity.this, RecyclerView.VERTICAL, false));
        rv.setAdapter(regulationItemAdapter);
    }

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, RegulationDetailsActivity.class);
        intent.putExtra("Id", id);
        context.startActivity(intent);
    }

    @OnClick({R.id.back_imgbtn, R.id.modfiy_btn, R.id.search_btn, R.id.delete_imgbtn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                if (StringUtils.isEmpty(search_autotv.getText().toString())) {
                    loadAllData();
                } else {
                    filtrationData(search_autotv.getText().toString());
                }
                break;
            case R.id.delete_imgbtn:
                search_autotv.setText("");
                break;
            case R.id.back_imgbtn:
                RegulationDetailsActivity.this.finish();
                break;
            case R.id.modfiy_btn:
                NewRegulationActivity.startForResult(RegulationDetailsActivity.this, id, REQUEST_CODE);
                break;
        }
    }
}
