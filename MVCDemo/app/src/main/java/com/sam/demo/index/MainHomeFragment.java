package com.sam.demo.index;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.sam.demo.R;
import com.sam.demo.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;


/**
 * 首页
 */

public class MainHomeFragment extends BaseFragment {
    @Bind(R.id.rev_menu)
    RecyclerView recyclerView;

    @Bind(R.id.myWebview)
    WebView myWebview;
    @Bind(R.id.myScrollView)
    ScrollView myScrollView;
    @Bind(R.id.imgScanLogin)
    ImageView imgScanLogin;

    @Bind(R.id.mainRefreshLayout)
    SwipeRefreshLayout mainRefreshLayout;


    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(this, R.layout.fragment_main_home);
    }

    private void init() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
    }
}
