package com.sam.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sam.demo.R;
import com.sam.demo.base.BaseActivity;
import com.sam.demo.base.LoginActivity;
import com.sam.demo.base.LoginByFingerActivity;
import com.sam.demo.eventbus.CheckVersionEvent;
import com.sam.demo.mine.SysSettingActivity;
import com.sam.demo.constants.Data;
import com.sam.demo.constants.Urls;
import com.sam.demo.entity.MainPermission;
import com.sam.demo.index.MainHomeFragment;
import com.sam.demo.mine.MainMeFragment;
import com.sam.demo.msg.MainNotifFragment;
import com.sam.demo.util.AppUpdateManager;
import com.sam.demo.util.MyStringCallback;
import com.sam.demo.util.SharedPreferencesUtils;
import com.sam.demo.util.ToastUtil;
import com.sam.demo.widget.FragmentTabHost;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.Bind;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.sam.demo.util.NotificationUtils.notificationManager;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/1
 * 描    述：首页
 * 修订历史：
 * ================================================
 */

public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener {
    String[] tab_names ;
    public static final String fromTag="fromlogin";
    public static boolean fromLogin=true;//从登录页跳转至此
    private String notifyOpen="";//是否是从通知栏点击进来的

    private View indicator = null;

    public View IsNewVersionView;//是否是新版本标志所在的视图

    public static TextView tvUnReadNum;//未读消息控件

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    private Map<Integer, MainPermission> mainPermissionMap;
    /** 子类可以重写决定是否使用透明状态栏 */
    @Override
    protected boolean translucentStatusBar() {
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_main);
        fromLogin=getIntent().getBooleanExtra(fromTag,true);
        notifyOpen=getIntent().getStringExtra("notifyOpen")==null?"":getIntent().getStringExtra("notifyOpen");
        if (!fromLogin)
        {
            boolean temLoginFinger = (boolean) SharedPreferencesUtils.getParam(MainActivity.this, SysSettingActivity.isfinger, false);
            if (temLoginFinger)
            {
                Intent intent = new Intent(MainActivity.this, LoginByFingerActivity.class);
                startActivity(intent);
            }
        }
        AppUpdateManager.getInstance().setContext(this);
        AppUpdateManager.getInstance().check(false);
        //-------------------------------------
        init();
        initTab();
    }

    private void init() {
        tab_names= getResources().getStringArray(R.array.main_tab);
    }
    /**
     * @description double click exit application
     */
    private long exitTime = 0;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showToast(this, R.string.two_click_exit);
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(this);
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initTab() {
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        indicator = getIndicatorView(tab_names[0], R.layout.item_main_tab_home);
        mTabHost.addTab(mTabHost.newTabSpec("0").setIndicator(indicator), MainHomeFragment.class, null);
        indicator = getIndicatorView(tab_names[1], R.layout.item_main_tab_notify);
        mTabHost.addTab(mTabHost.newTabSpec("1").setIndicator(indicator), MainNotifFragment.class, null);
        indicator = getIndicatorView(tab_names[2], R.layout.item_main_tab_me);
        mTabHost.addTab(mTabHost.newTabSpec("2").setIndicator(indicator), MainMeFragment.class, null);


        mTabHost.setOnTabChangedListener(MainActivity.this);
        mTabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);
    }


    private View getIndicatorView(String tittle, int resId) {
        View v = getLayoutInflater().inflate(resId, null);
        TextView tv = (TextView) v.findViewById(R.id.tabText);
        tv.setText(tittle);
        if (tab_names[2].equals(tittle))
        {
            IsNewVersionView=v;
        }else
        {
            IsNewVersionView=null;
            if (tab_names[1].equals(tittle))//消息模块
            {
                tvUnReadNum= (TextView) v.findViewById(R.id.tvMsgCount);
            }
        }
        return v;
    }

    /**
     * 底部Tab点击事件监听
     *
     * @param tabId
     */
    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (notificationManager!=null)
            notificationManager.cancelAll();
        ShortcutBadger.removeCount(MainActivity.this);
    }
    private static final int TIME_OUT = 1;


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
