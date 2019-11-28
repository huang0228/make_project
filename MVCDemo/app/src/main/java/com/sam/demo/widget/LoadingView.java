package com.sam.demo.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.sam.demo.R;

/**
 * Created by Administrator on 2018/3/26.
 */

public class LoadingView extends ProgressDialog {

    private boolean isCancelEnable=true;

    public LoadingView(Context context) {
        super(context);
    }
    public LoadingView(Context context, int theme,boolean isCancel) {
        super(context, theme);
        this.isCancelEnable=isCancel;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }
    private void init(Context context) {
        setCancelable(isCancelEnable);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.layout_loading);//loading的xml文件
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
    @Override
    public void show() {//开启
        super.show();
    }
    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }
}
