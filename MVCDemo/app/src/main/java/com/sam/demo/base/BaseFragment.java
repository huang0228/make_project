package com.sam.demo.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sam.demo.AppManager;
import com.sam.demo.R;
import com.sam.demo.base.BaseActivity;
import com.sam.demo.listener.PermissionListener;
import com.sam.demo.util.MyStringCallback;
import com.sam.demo.widget.LoadingView;
import com.bumptech.glide.Glide;

import butterknife.ButterKnife;


/**
 * ===============================================
 * 版    本：1.0.1
 * 创建日期：2018/3/1
 * 描    述：所有Fragent的基类
 * 修订历史：
 * ================================================
 */

public abstract class BaseFragment extends Fragment implements MyStringCallback.RequestResultListener {
    public static final String TAG = "BaseFragment";

    public final int pageSize = 20;
    //下拉刷新回弹时间
    public final int REFRESH_COMPLETE = 500;
    private LayoutInflater inflater;
    protected View contentView;
    protected Context context;
    private ViewGroup container;
    public static final String INTENT_STRING_TABNAME = "intent_String_tabname";
    public static final String INTENT_INT_INDEX = "intent_int_index";


    public void finishActivity(View view) {
        AppManager.getAppManager().finishActivity();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        onCreateView(savedInstanceState);
        return contentView;
    }


    protected abstract void onCreateView(Bundle savedInstanceState);

    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }
    public void displayImage(String url, ImageView imageView) {
        Glide.with(getApplicationContext())//
                .load(url)//
                .error(R.mipmap.ic_launcher_round)//
                .into(imageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contentView = null;
        container = null;
        inflater = null;
    }

    public Context getApplicationContext() {
        return context;
    }

    public void setContentView(Object handle,int layoutResID) {
        setContentView(handle, inflater.inflate(layoutResID, container, false));
    }

    public void setContentView(Object handle,View view) {
        contentView = view;
        ButterKnife.bind(this, contentView);
    }

    public View getContentView() {
        return contentView;
    }

    public View findViewById(int id) {
        if (contentView != null)
            return contentView.findViewById(id);
        return null;
    }

    // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-
    @Override
    public void onDetach() {
        super.onDetach();

    }




    LoadingView mProgressDialog;
    BaseActivity activity;
    /*
     * 显示进度框
     */
    public void showProgressDialog(String msg) {
        activity= (BaseActivity)this.getActivity();
        activity.showProgressDialog(msg);
    }
    public void showProgressDialog(String msg ,boolean cancleble) {
        activity= (BaseActivity)this.getActivity();
        activity.showProgressDialog(msg,cancleble);
    }
    /*
     * 隐藏进度框
     */
    public void cancelProgressDialog() {
        activity= (BaseActivity)this.getActivity();
        activity.cancelProgressDialog();
//        if(mProgressDialog!=null&&mProgressDialog.isShowing())
//            mProgressDialog.cancel();
    }






    @Override
    public void onError(String str, int flag) {

    }

    @Override
    public void onSuccess(String str, int flag) {

    }
    /**
     * 请求权限
     * @param mActivity
     * @param permissions
     */
    public void requestRunPermission(Activity mActivity, String[] permissions, PermissionListener mListener){
        ((BaseActivity)mActivity).requestRunPermission(mActivity,permissions,mListener);
    }
}
