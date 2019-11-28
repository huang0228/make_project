package com.sam.demo.base;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.adapter.GuideAdapter;
import com.sam.demo.base.BaseActivity;
import com.sam.demo.base.LoginActivity;
import com.sam.demo.constants.Data;
import com.sam.demo.util.SharedPreferencesUtils;
import com.sam.demo.util.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 版    本：1.0.1
 * 创建日期：2018/5/2
 * 描    述：引导页
 * 修订历史：
 * ================================================
 */

public class WelcomeGuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener,ComponentCallbacks2 {
    private ViewPager mViewPager;
    private TextView tv_guideLogin;
    private List<Integer> guideImg;
    private GuideAdapter adapter;
    private FrameLayout layout_root;
    @Bind(R.id.tab_home_banner_layout)
    LinearLayout bannerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomeguide_activity);
        hideBottomUIMenu();
        mViewPager = (ViewPager) findViewById(R.id.viewPager_guide);
        mViewPager.addOnPageChangeListener(this);
        tv_guideLogin = (TextView) findViewById(R.id.tv_guide_login);
        guideImg = new ArrayList<>();
        addimg();
        addBanner(guideImg.size());
        adapter = new GuideAdapter(this, guideImg);
        mViewPager.setAdapter(adapter);
    }

    private void addimg() {
        guideImg.add(R.drawable.guide1);
        guideImg.add(R.drawable.guide1);
        guideImg.add(R.drawable.guide1);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        if (i == guideImg.size() - 1 && i1 == 0) {
            tv_guideLogin.setVisibility(View.VISIBLE);
        } else {
            tv_guideLogin.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageSelected(int position) {
        if (bannerLayout.getChildCount() > position) {
            for (int i = 0; i < bannerLayout.getChildCount(); i++) {
                ImageView iv = (ImageView) bannerLayout.getChildAt(i);
            }
        }
    }

    private void addBanner(int num) {
        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); // ,
        lp4.setMargins(Utils.dp2px(this, 10), 0, 0, 0);
        // bannerLayout.remove
        for (int i = bannerLayout.getChildCount() - 1; i >= 0; i--) {
            bannerLayout.removeViewAt(i);
        }
        for (int i = 0; i < num; i++) {
            ImageView iv = new ImageView(this);
            //iv.setImageResource(R.drawable.bg_guide_banner_dian_normall);
            if (i == 0)
                //iv.setImageResource(R.drawable.bg_guide_banner_dian_press);
            iv.setLayoutParams(lp4);
            bannerLayout.addView(iv);
        }
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @OnClick({R.id.skip, R.id.tv_guide_login})
    public void onClidk(View view) {
        switch (view.getId()) {
            case R.id.skip:
            case R.id.tv_guide_login:
                SharedPreferencesUtils.setParam(this, Data.FIRST_OPEN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }



    /**
     * 华为等手机内置键盘
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //for new api versions.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.with(MyApplication.AppContent).onTrimMemory(level);
    }
}
