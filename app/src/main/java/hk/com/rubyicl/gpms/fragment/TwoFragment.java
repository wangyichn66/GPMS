package hk.com.rubyicl.gpms.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.RecyclerViewSpacesItemDecoration;
import hk.com.rubyicl.gpms.adapter.RegulationAdapter;
import hk.com.rubyicl.gpms.activity.RegulationDetailsActivity;
import hk.com.rubyicl.gpms.entity.RegulationEntity;
import hk.com.rubyicl.gpms.entity.RegulationItemEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/3 下午 05:35
 *     description:
 *  <pre>
 */
public class TwoFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.search_btn)
    Button search_btn;
    @BindView(R.id.search_autotv)
    AutoCompleteTextView search_autotv;
    @BindView(R.id.sml)
    SmartRefreshLayout sml;
    @BindView(R.id.material_empty_layout)
    View material_empty_layout;
    @BindView(R.id.delete_imgbtn)
    ImageButton delete_imgbtn;
    private RegulationAdapter regulationAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_two;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadAllData();
    }


    private void initView() {
        regulationAdapter = new RegulationAdapter();
        regulationAdapter.setOnItemClickListener(new RegulationAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                RegulationDetailsActivity.start(
                    getContext(),
                    regulationAdapter.getRegulationEntityList().get(position).getId()
                );
            }
        });
        regulationAdapter.setOnItemLongClickListener(new RegulationAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                new AlertDialog.Builder(getContext())
                    .setTitle("确认删除")
                    .setMessage("确认删除本行吗？删除后不可撤销！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            materialAdapter.MyRemoveItem(position);
                            if (LitePal.delete(RegulationEntity.class, regulationAdapter.getRegulationEntityList().get(position).getId()) > 0) {
                                ToastUtils.showShort("删除成功");
                            } else {
                                ToastUtils.showShort("删除失败");
                            }
                            loadAllData();
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
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 5);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 0);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 0);
        rv.addItemDecoration(new RecyclerViewSpacesItemDecoration(getContext(), LinearLayoutManager.VERTICAL, stringIntegerHashMap));
        rv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rv.setAdapter(regulationAdapter);

        sml.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadAllData();
                ToastUtils.showShort("刷新成功");
                refreshLayout.finishRefresh();
            }
        });
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

    private void hebing(List<RegulationEntity> regulationEntityList, List<RegulationItemEntity> regulationItemEntityList) {
        for (RegulationItemEntity regulationItemEntity : regulationItemEntityList) {
            if (!isHasData(regulationEntityList, regulationItemEntity)) {
                regulationEntityList.add(LitePal.find(RegulationEntity.class, regulationItemEntity.getRegulationEntity().getId()));
            }
        }
    }

    private boolean isHasData(List<RegulationEntity> regulationEntityList, RegulationItemEntity regulationItemEntity) {
        for (RegulationEntity regulationEntity : regulationEntityList) {
            if (regulationItemEntity.getRegulationEntity().getId() == regulationEntity.getId()) {
                return true;
            }
        }
        return false;
    }

    private void filtrationData(String like) {
        String like_str = "%" + like + "%";
        List<RegulationEntity> regulationEntityList =
            LitePal.where("name like ? or remarks like ?",
                like_str, like_str)
                .find(RegulationEntity.class,true);
        List<RegulationItemEntity> regulationItemEntityList =
            LitePal.where("substances_name_cn like ? or substances_name_eg like ? " +
                "or CAS_No like ? or threshold like ?", like_str, like_str, like_str, like_str).find(RegulationItemEntity.class, true);
        hebing(regulationEntityList, regulationItemEntityList);

        ToastUtils.showLong("查找到%d条数据", regulationEntityList.size());
        LogUtils.d(regulationEntityList);
        regulationAdapter.setData(regulationEntityList);
        if (regulationEntityList.size() == 0) {
            TextView empty_layout_tv = material_empty_layout.findViewById(R.id.empty_layout_tv);
            empty_layout_tv.setText(getString(R.string.regulation_serach_empty));
            material_empty_layout.setVisibility(View.VISIBLE);
        } else {
            material_empty_layout.setVisibility(View.GONE);
        }
    }

    private void loadAllData() {
        List<RegulationEntity> regulationEntityList = LitePal.findAll(RegulationEntity.class, true);
//        LogUtils.d(regulationEntityList);
        regulationAdapter.setData(regulationEntityList);
        if (regulationEntityList.size() == 0) {
            TextView empty_layout_tv = material_empty_layout.findViewById(R.id.empty_layout_tv);
            empty_layout_tv.setText(getString(R.string.regulation_empty));
            material_empty_layout.setVisibility(View.VISIBLE);
        } else {
            material_empty_layout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.search_btn, R.id.delete_imgbtn, R.id.info_iv})
    void onViewClick(View view) {
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
            case R.id.info_iv:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage(
                        getString(
                            R.string.tips, "\n2.可通过法规名称、备注以及法规保含的物质名称、Substance Name、CAS No.、限制要求来过滤法规"
                        )
                    )
                    .setPositiveButton("ok", null)
                    .setCancelable(true)
                    .show();
                break;
        }
    }

}
