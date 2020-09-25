package hk.com.rubyicl.gpms.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.entity.RegulationItemEntity;
import hk.com.rubyicl.gpms.entity.SubstanceEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/25 10:02
 *     description:
 *  <pre>
 */
public class NewRequlationItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnItemLongClickListener onItemClicklongListener;
    private final int ITEM_CONTENT = 1;
    private final int ITEM_FOOT = 2;
    private List<RegulationItemEntity> regulationItemEntities = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_FOOT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_foot_layout, parent, false);
            return new NewRequlationItemAdapter.FootViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_requlation_item, parent, false);
        return new NewRequlationItemAdapter.ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_FOOT) {
            NewRequlationItemAdapter.FootViewHolder footViewHolder = (NewRequlationItemAdapter.FootViewHolder) holder;
            footViewHolder.foot_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    regulationItemEntities.add(new RegulationItemEntity());
                    notifyItemInserted(regulationItemEntities.size() - 1);
                    ToastUtils.showShort("添加的Item下标为:" + (regulationItemEntities.size() - 1));
                }
            });
        } else if (getItemViewType(position) == ITEM_CONTENT) {
            NewRequlationItemAdapter.ContentViewHolder contentViewHolder = (NewRequlationItemAdapter.ContentViewHolder) holder;
            if (onItemClicklongListener != null)
                contentViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClicklongListener.onItemLongClicked(position);
                        return true;
                    }
                });
            contentViewHolder.et1.setText(regulationItemEntities.get(position).getSubstances_name_cn());
            contentViewHolder.et2.setText(regulationItemEntities.get(position).getSubstances_name_eg());
            contentViewHolder.et3.setText(regulationItemEntities.get(position).getCAS_No());
            contentViewHolder.et4.setText(regulationItemEntities.get(position).getThreshold());
            contentViewHolder.et1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    regulationItemEntities.get(position).setSubstances_name_cn(s.toString());
                }
            });
            contentViewHolder.et2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    regulationItemEntities.get(position).setSubstances_name_eg(s.toString());
                }
            });
            contentViewHolder.et3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    regulationItemEntities.get(position).setCAS_No(s.toString());
                }
            });
            contentViewHolder.et4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    regulationItemEntities.get(position).setThreshold(s.toString());
                }
            });
        }
    }

    public void MyRemoveItem(int position) {
        ToastUtils.showShort("删除下标为: " + position + "的Item");
        regulationItemEntities.remove(position);
        notifyItemRemoved(position);
//        notifyItemRangeChanged(0, substanceEntities.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == regulationItemEntities.size()) return ITEM_FOOT;
        else return ITEM_CONTENT;
    }

    @Override
    public int getItemCount() {
        return regulationItemEntities.size() + 1;
    }

    public void setRegulationItemEntities(List<RegulationItemEntity> regulationItemEntities) {
        this.regulationItemEntities = regulationItemEntities;
        notifyDataSetChanged();
    }

    public List<RegulationItemEntity> getRegulationItemEntities() {
        return regulationItemEntities;
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.et1)
        public EditText et1;
        @BindView(R.id.et2)
        public EditText et2;
        @BindView(R.id.et3)
        public EditText et3;
        @BindView(R.id.et4)
        public EditText et4;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class FootViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.foot_layout)
        public ConstraintLayout foot_layout;

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemLongClickListener(NewRequlationItemAdapter.OnItemLongClickListener listener) {
        this.onItemClicklongListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(int position);
        //void onItemLongClick(View view, int position);
    }
}
