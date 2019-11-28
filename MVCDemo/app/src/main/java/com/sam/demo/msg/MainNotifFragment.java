package com.sam.demo.msg;

import android.os.Bundle;

import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.entity.User;
import com.sam.demo.base.BaseFragment;
import com.sam.demo.util.Utils;

import butterknife.Bind;
import me.dkzwm.widget.srl.SmoothRefreshLayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class MainNotifFragment extends BaseFragment {

    @Bind(R.id.li_notify_bar_title)
    LinearLayout li_notify_bar_title;
    @Bind(R.id.tv_notify_title)
    TextView tvActionbarTitle;
    @Bind(R.id.imgNotifyArrow)
    ImageView imgNotifyArrow;
    @Bind(R.id.imgNotifySearch)
    ImageView imgNotifySearch;

    @Bind(R.id.tv_view)
    TextView tv_view;
    @Bind(R.id.ll_view)
    LinearLayout ll_view;
    @Bind(R.id.id_recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmoothRefreshLayout refreshLayout;


    @Bind(R.id.rel_nodata)
    RelativeLayout rel_nodata;

    @Bind(R.id.liAllMsg)
    LinearLayout liAllMsg;
    @Bind(R.id.liUnReadMsg)
    LinearLayout liUnReadMsg;
    @Bind(R.id.liReadMsg)
    LinearLayout liReadMsg;
    @Bind(R.id.tvAllMsg)
    TextView tvAllMsg;
    @Bind(R.id.tvUnReadMsg)
    TextView tvUnReadMsg;
    @Bind(R.id.tvReadMsg)
    TextView tvReadMsg;
    @Bind(R.id.ViewAllMsg)
    View ViewAllMsg;
    @Bind(R.id.ViewUnReadMsg)
    View ViewUnReadMsg;
    @Bind(R.id.ViewReadMsg)
    View ViewReadMsg;

    private String userId = "";

    private boolean isDownRefresh = true;

    private android.app.AlertDialog alertDialog, delDialog;
    private View dialogView;


    private PopupWindow popTypeWindow;//类别选择窗口
    View popView;
    private LinearLayout rl_pop_root;

    private List<com.sam.demo.entity.Menu> mList;//搜索列表集合
    private String[] array = {"所有类型"};
    private String[] arrayValue = {"",};

    private String selectTyeCode = "";//选择的告警类别code值
    private boolean isMyFavorite = false;//是否查询我的收藏标示
    private boolean isStoreChecked = false;//是否已收藏

    User user = null;//用户基本信息

    int msgCount = 0;//消息总数

    private List<LinearLayout> mListLinearLayout;
    private List<TextView> mListTextView;
    private List<View> mListView;
    private boolean hasRead = false;//0未读，1 已读
    private String hasReadFlag = "0x00";

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(this, R.layout.fragment_main_report);

        tv_view.setHeight(Utils.getStatusHeight(getActivity()));
        tvActionbarTitle.setText(array[0]);

        user = MyApplication.getInstance().getUser();

    }
}

