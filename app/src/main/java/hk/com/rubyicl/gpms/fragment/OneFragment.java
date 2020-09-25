package hk.com.rubyicl.gpms.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import hk.com.rubyicl.gpms.adapter.MaterialAdapter;
import hk.com.rubyicl.gpms.MyUtils;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.RecyclerViewSpacesItemDecoration;
import hk.com.rubyicl.gpms.activity.MaterialDetailsActivity;
import hk.com.rubyicl.gpms.entity.MaterialEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/3 下午 05:35
 *     description:
 *  <pre>
 */
public class OneFragment extends BaseFragment {
    @BindView(R.id.search_et)
    EditText search_et;
    @BindView(R.id.delete_imgbtn)
    ImageButton delete_imgbtn;
    @BindView(R.id.search_btn)
    Button search_btn;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.material_empty_layout)
    View material_empty_layout;
    @BindView(R.id.sml)
    SmartRefreshLayout sml;
    @BindView(R.id.info_iv)
    ImageView info_iv;
    MaterialAdapter materialAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_one;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadAllData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadAllData();
    }

    private void loadAllData() {
        List<MaterialEntity> materialEntityList = LitePal.findAll(MaterialEntity.class, true);
        LogUtils.d(materialEntityList);
        materialAdapter.setData(materialEntityList);
        if (materialEntityList.size() == 0) {
            TextView empty_layout_tv = material_empty_layout.findViewById(R.id.empty_layout_tv);
            empty_layout_tv.setText(getString(R.string.material_empty));
            material_empty_layout.setVisibility(View.VISIBLE);

        } else {
            material_empty_layout.setVisibility(View.GONE);
        }
    }


    private void filtrationData(String like) {
        String like_str = "%" + like + "%";
        List<MaterialEntity> materialEntityList =
            LitePal.where("name like ? or brand like ? or type like ? or compliance like ?", like_str, like_str, like_str, like_str)
                .find(MaterialEntity.class);
        ToastUtils.showLong("查找到%d条数据", materialEntityList.size());
        LogUtils.d(materialEntityList);
        materialAdapter.setData(materialEntityList);
        if (materialEntityList.size() == 0) {
            TextView empty_layout_tv = material_empty_layout.findViewById(R.id.material_empty_layout);
            empty_layout_tv.setText(getString(R.string.material_serach_empty));
            material_empty_layout.setVisibility(View.VISIBLE);
        } else {
            material_empty_layout.setVisibility(View.GONE);
        }
    }

    private void initView() {
        sml.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadAllData();
                ToastUtils.showShort("刷新成功");
                refreshLayout.finishRefresh();
            }
        });
        materialAdapter = new MaterialAdapter();
        materialAdapter.setOnItemClickListener(new MaterialAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                MaterialDetailsActivity.start(
                    getContext(),
                    materialAdapter.getMaterialEntityList().get(position).getId()
                );
            }
        });
        materialAdapter.setOnItemLongClickListener(new MaterialAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                new AlertDialog.Builder(getContext())
                    .setTitle("确认删除")
                    .setMessage("确认删除本行吗？删除后不可撤销！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            materialAdapter.MyRemoveItem(position);
                            if (LitePal.delete(MaterialEntity.class, materialAdapter.getMaterialEntityList().get(position).getId()) > 0) {
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
        rv.setAdapter(materialAdapter);
        search_et.addTextChangedListener(new TextWatcher() {
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

    @OnClick({R.id.search_btn, R.id.delete_imgbtn, R.id.info_iv})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                if (StringUtils.isEmpty(search_et.getText().toString())) {
                    loadAllData();
                } else {
                    filtrationData(search_et.getText().toString());
                }
                break;
            case R.id.delete_imgbtn:
                search_et.setText("");
                break;
            case R.id.info_iv:
//                TipsPopupWindow tipsPopupWindow = new TipsPopupWindow(getActivity());
//                tipsPopupWindow.setPopupGravity(Gravity.CENTER);
////                tipsPopupWindow.setBackgroundColor(Color.TRANSPARENT);
////                tipsPopupWindow.setBlurBackgroundEnable(true);
//                tipsPopupWindow.showPopupWindow();
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage(
                        getString(
                            R.string.tips, "\n2." + MyUtils.getDebugDBAddressLog(getActivity())
                                + "\n将手机和电脑连接到同一局域网或者wifi\n然后输入上面这个地址就可以调试数据库"
                                + "\n3.可通过物料名称、品牌、类型、法规搜索对应物料"
                        )
                    )
                    .setPositiveButton("ok", null)
                    .setCancelable(true)
                    .show();
                break;
        }
    }
}
