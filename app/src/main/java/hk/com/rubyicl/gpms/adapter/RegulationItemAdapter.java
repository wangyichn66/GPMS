package hk.com.rubyicl.gpms.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.entity.RegulationItemEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/22 16:34
 *     description:
 *  <pre>
 */
public class RegulationItemAdapter extends RecyclerView.Adapter<RegulationItemAdapter.ViewHolder> {
    List<RegulationItemEntity> regulationItemEntityList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.regulation_details_item, parent, false);
        return new RegulationItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (regulationItemEntityList != null) {
            RegulationItemEntity regulationItemEntity = regulationItemEntityList.get(position);
//            holder.tv1.setText(regulationItemEntity.getNo());
            holder.tv2.setText(regulationItemEntity.getSubstances_name_cn());
            holder.tv3.setText(regulationItemEntity.getSubstances_name_eg());
            holder.tv4.setText(regulationItemEntity.getCAS_No());
            holder.tv5.setText(regulationItemEntity.getThreshold());
        }
    }

    @Override
    public int getItemCount() {
        return regulationItemEntityList == null ? 0 : regulationItemEntityList.size();
    }

    public void setData(List<RegulationItemEntity> list) {
        this.regulationItemEntityList = list;
        notifyDataSetChanged();
    }

    public List<RegulationItemEntity> getRegulationItemEntityList() {
        return regulationItemEntityList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv1)
//        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;
        @BindView(R.id.tv4)
        TextView tv4;
        @BindView(R.id.tv5)
        TextView tv5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
