package com.sam.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.demo.R;
import com.sam.demo.entity.MainPermission;
import com.sam.demo.interfaces.IOnItemClickListener;
import com.sam.demo.util.ListUtils;

import java.util.List;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/4/28
 * 描    述：首页权限
 * 修订历史：
 * ================================================
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.Holder> implements View.OnClickListener {

    private Context context;
    private List<MainPermission> mainPermissions;
    LayoutInflater inflater;
    private IOnItemClickListener iOnItemClickListener;

    public MenuAdapter(Context context, LayoutInflater inflater, List<MainPermission> mainPermissions) {
        this.context = context;
        this.inflater = inflater;
        this.mainPermissions = mainPermissions;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home_menu, parent, false);
        view.setOnClickListener(this);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.itemView.setTag(position);
        holder.tvMenu.setText(mainPermissions.get(position).getTabName());
        holder.ivMenu.setImageResource(mainPermissions.get(position).getLayoutId());
    }
    public void setIOnItemClickListener(IOnItemClickListener onItemClickListener){
        iOnItemClickListener=onItemClickListener;

    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(mainPermissions) ? 0 : mainPermissions.size();
    }

    @Override
    public void onClick(View v) {
        if(iOnItemClickListener!=null){
            iOnItemClickListener.onClick(v,((int) v.getTag()));
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvMenu;
        private ImageView ivMenu;

        public Holder(View itemView) {
            super(itemView);
            tvMenu = (TextView) itemView.findViewById(R.id.tv_menu);
            ivMenu = (ImageView) itemView.findViewById(R.id.iv_menu);

        }

    }
}
