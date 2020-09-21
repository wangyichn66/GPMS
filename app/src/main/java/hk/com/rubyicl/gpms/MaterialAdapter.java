package hk.com.rubyicl.gpms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.rubyicl.gpms.entity.MaterialEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/4 上午 10:52
 *     description: 物料的适配器给RecycleView提供数据来源
 *  <pre>
 */
public class MaterialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MaterialAdapter.OnItemClickListener onItemClickListener;
    private MaterialAdapter.OnItemLongClickListener onItemLongClickListener;
    List<MaterialEntity> materialEntityList;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_list_item, parent, false);
        return new MaterialAdapter.ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindContentViewHolder((ContentViewHolder) holder, position);
    }

    private void bindContentViewHolder(ContentViewHolder holder, int position) {
        MaterialEntity materialEntity = materialEntityList.get(position);
        holder.title_tv.setText(materialEntity.getName());
        holder.type_tv.setText(materialEntity.getType());
        holder.time_tv.setText(materialEntity.getTime());
        holder.brand_tv.setText(materialEntity.getBrand());
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

    public void setData(List<MaterialEntity> materialEntityList) {
        this.materialEntityList = materialEntityList;
        notifyDataSetChanged();
    }

    public void MyRemoveItem(int position) {
        ToastUtils.showShort("删除下标为: " + position + "的Item");
        if (LitePal.delete(MaterialEntity.class, materialEntityList.get(position).getId()) > 0) {
            materialEntityList.remove(position);
            notifyItemRemoved(position);
            ToastUtils.showShort("删除成功");
        } else {
            ToastUtils.showShort("删除失败");
        }

    }

    public List<MaterialEntity> getMaterialEntityList() {
        return materialEntityList;
    }

    @Override
    public int getItemCount() {
//        return materialEntityList == null ? 0 : materialEntityList.size() + 1; //因为一直有底部添加栏所以加1
        return materialEntityList == null ? 0 : materialEntityList.size();
    }

    public void setOnItemClickListener(MaterialAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.onItemLongClickListener = longClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_tv)
        TextView title_tv;
        @BindView(R.id.brand_tv)
        TextView brand_tv;
        @BindView(R.id.type_tv)
        TextView type_tv;
        @BindView(R.id.time_tv)
        TextView time_tv;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
