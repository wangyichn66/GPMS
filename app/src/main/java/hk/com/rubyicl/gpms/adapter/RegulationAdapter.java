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
import hk.com.rubyicl.gpms.entity.RegulationEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/22 9:31
 *     description:
 *  <pre>
 */
public class RegulationAdapter extends RecyclerView.Adapter<RegulationAdapter.ViewHolder> {
    private List<RegulationEntity> regulationEntityList;
    private RegulationAdapter.OnItemClickListener onItemClickListener;
    private RegulationAdapter.OnItemLongClickListener onItemLongClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.regulation_item, parent, false);
        return new RegulationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegulationEntity regulationEntity = regulationEntityList.get(position);
        holder.regulation_item_name_tv.setText(regulationEntity.getName());
        holder.regulation_item_remark_tv.setText(regulationEntity.getRemarks());
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(position);
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return regulationEntityList == null ? 0 : regulationEntityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.regulation_item_name_tv)
        TextView regulation_item_name_tv;
        @BindView(R.id.regulation_item_remark_tv)
        TextView regulation_item_remark_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setData(List<RegulationEntity> regulationEntityList) {
        this.regulationEntityList = regulationEntityList;
        notifyDataSetChanged();
    }

    public List<RegulationEntity> getRegulationEntityList() {
        return regulationEntityList;
    }

    public void setOnItemClickListener(RegulationAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(RegulationAdapter.OnItemLongClickListener longClickListener) {
        this.onItemLongClickListener = longClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}
