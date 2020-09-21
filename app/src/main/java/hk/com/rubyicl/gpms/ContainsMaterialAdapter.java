package hk.com.rubyicl.gpms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.rubyicl.gpms.entity.SubstanceEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/8 上午 11:40
 *     description:
 *  <pre>
 */
public class ContainsMaterialAdapter extends RecyclerView.Adapter<ContainsMaterialAdapter.ViewHolder> {
    List<SubstanceEntity> substanceEntities = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contains_material_item, parent, false);
        return new ContainsMaterialAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubstanceEntity substanceEntity = substanceEntities.get(position);
        LogUtils.d(substanceEntity);
        holder.contains_material_tv1.setText(substanceEntity.getName());
        holder.contains_material_tv2.setText(substanceEntity.getCas_no());
        holder.contains_material_tv3.setText(substanceEntity.getContent());
        holder.contains_material_tv4.setText(substanceEntity.getUsage());
    }

    @Override
    public int getItemCount() {
        return substanceEntities == null ? 0 : substanceEntities.size();
    }

    public void setSubstanceEntities(List<SubstanceEntity> substanceEntities) {
        this.substanceEntities = substanceEntities;
        notifyDataSetChanged();
    }

   public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contains_material_tv1)
        TextView contains_material_tv1;
        @BindView(R.id.contains_material_tv2)
        TextView contains_material_tv2;
        @BindView(R.id.contains_material_tv3)
        TextView contains_material_tv3;
        @BindView(R.id.contains_material_tv4)
        TextView contains_material_tv4;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
