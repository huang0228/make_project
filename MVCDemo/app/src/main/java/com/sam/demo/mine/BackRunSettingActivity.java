package com.sam.demo.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.demo.R;
import com.sam.demo.base.BaseActivity;
import com.sam.demo.entity.Menu;
import com.sam.demo.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2019/2/25
 * <p>
 * 描述：后台运行时间设定
 */
public class BackRunSettingActivity extends BaseActivity {
    public static final String isBgRunFlag="bgRunTime";
    public static final String defaultTime="5";

    @Bind(R.id.rv_run_setting)
    RecyclerView rv_run_setting;
    @Bind(R.id.tv_actionbar_title)
    TextView tv_actionbar_title;
    @Bind(R.id.viewBottomLine)
    View viewBottomLine;

    private List<Menu> mList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_run_setting);
        viewBottomLine.setVisibility(View.GONE);
        tv_actionbar_title.setVisibility(View.VISIBLE);
        tv_actionbar_title.setText(getResources().getString(R.string.bg_run_label));

        String[] menus={this.getResources().getString(R.string.bg_run_time_1),
                this.getResources().getString(R.string.bg_run_time_2),
                this.getResources().getString(R.string.bg_run_time_3)};
        String[] menusCode={"5","10","15"};

        mList=new ArrayList<>();
        for (int i=0;i<menus.length;i++)
        {
            Menu menu=new Menu();
            menu.setMENU_ID(menusCode[i]);
            menu.setMENU_NAME(menus[i]);
            mList.add(menu);
        }
        rv_run_setting.setLayoutManager(new LinearLayoutManager(BackRunSettingActivity.this));
        rv_run_setting.setAdapter(new bgRunAdapter());
    }


    class bgRunAdapter extends RecyclerView.Adapter<bgRunAdapter.MyHolder>{

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(BackRunSettingActivity.this).inflate(R.layout.bg_run_time_item,parent,false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            holder.tvMenuName.setText(mList.get(position).getMENU_NAME());
            final Menu menu=mList.get(position);
            if (menu!=null)
            {
                String code=menu.getMENU_ID();
                if (code.equals(SharedPreferencesUtils.getParam(BackRunSettingActivity.this,isBgRunFlag,defaultTime)))
                {
                    holder.imgMenuRight.setVisibility(View.VISIBLE);
                    holder.tvMenuName.setTextColor(getResources().getColor(R.color.btnCheck));
                }else
                {
                    holder.imgMenuRight.setVisibility(View.INVISIBLE);
                    holder.tvMenuName.setTextColor(getResources().getColor(R.color.color_5a5e5d));
                }
                holder.tvMenuName.setText(menu.getMENU_NAME());
            }
            holder.rlItemReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.imgMenuRight.setVisibility(View.VISIBLE);
                    holder.tvMenuName.setTextColor(getResources().getColor(R.color.btnCheck));
                    SharedPreferencesUtils.setParam(BackRunSettingActivity.this,isBgRunFlag,menu.getMENU_ID());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList==null?0:mList.size();
        }

        public class  MyHolder extends RecyclerView.ViewHolder{
            private RelativeLayout rlItemReport;
            private TextView tvMenuName;
            private ImageView imgMenuRight;

            public MyHolder(View itemView) {
                super(itemView);
                rlItemReport= (RelativeLayout) itemView.findViewById(R.id.rlItemReport);
                imgMenuRight= (ImageView) itemView.findViewById(R.id.imgItemRight);
                tvMenuName= (TextView) itemView.findViewById(R.id.tv_item_report);
            }
        }
    }
}
