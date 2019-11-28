package com.sam.demo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sam.demo.AppManager;
import com.sam.demo.R;
import com.sam.demo.mine.SysSettingActivity;
import com.sam.demo.listener.PermissionListener;
import com.sam.demo.util.Anti_hijackingUtils;
import com.sam.demo.util.MyLog;
import com.sam.demo.util.MyStringCallback;
import com.sam.demo.util.PermissionUtils;
import com.sam.demo.util.SharedPreferencesUtils;
import com.sam.demo.util.StatusBarUtil;
import com.sam.demo.util.TimeUtils;
import com.sam.demo.util.WaterMarkUtil;
import com.sam.demo.widget.LoadingView;
import com.sam.demo.widget.SystemBarTintManager;
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;

import static com.sam.demo.mine.BackRunSettingActivity.defaultTime;
import static com.sam.demo.mine.BackRunSettingActivity.isBgRunFlag;
import static com.sam.demo.mine.SysSettingActivity.currentTime;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/2/22
 * 描    述：
 * 修订历史：
 * ================================================
 */

public abstract class BaseActivity extends AppCompatActivity implements MyStringCallback.RequestResultListener {
    public final int pageSize = 15;
    //加载框
    private LoadingView mProgressDialog;
    //权限监听
    private PermissionListener mListener;
    private final String TAG = "BaseActivity";
    protected String ClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClassName = getClass().getSimpleName();
        MyLog.d(TAG, ClassName);
        AppManager.getAppManager().addActivity(this);
        PushAgent.getInstance(this).onAppStart();
//        initSystemBarTint();
        StatusBarUtil.setStatusBarLightMode(getWindow());

        SharedPreferencesUtils.setParam(this,currentTime,Long.parseLong("0"));//应用在前台时清除时间记录
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert im != null;
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onResume() {
        Anti_hijackingUtils.getinstance().onResume();
        fingerInvalidateByRunBg();
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        Anti_hijackingUtils.getinstance().onPause(this);
        super.onPause();
    }

    public void finishActivity(View view) {
        AppManager.getAppManager().finishActivity(this);
    }

    private int runTime =0;
    private long lastPaseTime = 0;//记录进入后台的时间
    private Intent intent = null;
    //前后台切换是否指纹验证判断
    private void fingerInvalidateByRunBg()
    {
        boolean temLoginFinger = false;
        String getTime= (String) SharedPreferencesUtils.getParam(BaseActivity.this,isBgRunFlag,defaultTime);
        runTime=Integer.parseInt(getTime)*1000 * 60;
        temLoginFinger = (boolean) SharedPreferencesUtils.getParam(BaseActivity.this, SysSettingActivity.isfinger, false);
        lastPaseTime = (long) SharedPreferencesUtils.getParam(BaseActivity.this, currentTime, Long.parseLong("0"));
        if (temLoginFinger)
        {
            if (lastPaseTime==0){
               /* intent = new Intent(BaseActivity.this, LoginByFingerActivity.class);
                startActivity(intent);*/
            }else if (TimeUtils.getCurrentDate().getTime() - lastPaseTime > runTime)
            {
                if (!AppManager.getAppManager().hasActivityExistTop(LoginByFingerActivity.class)){
                    intent = new Intent(BaseActivity.this, LoginByFingerActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    /**
     * 字体不随系统的文字大小的改变而改变
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            MyLog.d("竖屏");

        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            MyLog.d("横屏");

        }
        super.onConfigurationChanged(newConfig);
    }


    /**
     * 子类可以重写改变状态栏颜色
     */
    protected int setStatusBarColor() {
        return getDarkColorPrimary();
    }

    /**
     * 子类可以重写决定是否使用透明状态栏
     */
    protected boolean translucentStatusBar() {
        return false;
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0使用三方工具
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(setStatusBarColor());
        }
    }

    /**
     * 获取主题色
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取深主题色
     */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.color.colorPrimaryDark_line, typedValue, true);
        return typedValue.data;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        if (!setRequestedOrientation())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        if (!setRequestedOrientation())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        if (!setRequestedOrientation())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
    }

    /**
     * 子类可以重写决定是否可横屏
     */
    protected boolean setRequestedOrientation() {
        return false;
    }

    @Override
    public void onSuccess(String str, int flag) {

    }

    @Override
    public void onError(String str, int flag) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        WaterMarkUtil.showWatermarkView(this);
    }

    /*
     * 显示进度框
     */
    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingView(this, R.style.CustomDialog,true);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMessage(msg);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void showProgressDialog(String msg, boolean cancleble) {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingView(this, R.style.CustomDialog,cancleble);
            mProgressDialog.setCancelable(cancleble);
            mProgressDialog.setCanceledOnTouchOutside(cancleble);
            mProgressDialog.setMessage(msg);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }


    /*
     * 隐藏进度框
     */
    public void cancelProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        SharedPreferencesUtils.setParam(this,currentTime,Long.parseLong("0"));//应用在前台时清除时间记录
    }

    /**
     * 请求权限（在onResume中写会重复调用弹窗，之到允许权限）
     *
     * @param mActivity
     * @param permissions
     */
    public void requestRunPermission(Activity mActivity, String[] permissions, PermissionListener mListener) {
        this.mListener = mListener;
        new PermissionUtils().requestRunPermission(mActivity, permissions, mListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new PermissionUtils().requestPermissionsResult(requestCode, permissions, grantResults, mListener);
    }
}
