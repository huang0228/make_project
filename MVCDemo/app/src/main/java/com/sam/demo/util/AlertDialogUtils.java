package com.sam.demo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sam.demo.R;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2018/8/20
 * <p>
 * 描述：公共弹出框类
 */
public class AlertDialogUtils {

    public static AlertDialog showDialog;

    /**
     * 建立弹框对象
     * @param context
     * @param isCancel
     * @return
     */
   public static AlertDialog initAlertDialogView(Context context,boolean isCancel){
       try{
           showDialog = new AlertDialog.Builder(context).create();
           showDialog.show();
           showDialog.setCanceledOnTouchOutside(isCancel);
           showDialog.setCancelable(isCancel);
           //防止遮挡底部物理键
           //showDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
           return showDialog;
       }catch (Exception e){
           AlertDialog alertDialog=new AlertDialog.Builder(context).create();
           return alertDialog;
       }

   }

    /**
     * 设置弹出配置参数
     * @param context
     * @param mAlertDialog
     * @param view
     */
   public static void setAlertDialogConfig(Activity context, AlertDialog mAlertDialog,View view){
       Window window = mAlertDialog.getWindow();
       WindowManager windowManager = context.getWindowManager();
       Display display = windowManager.getDefaultDisplay();
       WindowManager.LayoutParams lParams = window.getAttributes();
       lParams.width = (int) (display.getWidth() * 0.85);
       window.setAttributes(lParams);
       window.setWindowAnimations(R.style.popWindowScaleAnim);
       window.setBackgroundDrawable(new BitmapDrawable());
       window.setContentView(view);
       window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
   }

    /**
     * 设置弹出配置参数
     * @param context
     * @param mAlertDialog
     * @param view
     */
    public static void setAlertDialogConfig(Activity context, AlertDialog mAlertDialog,View view,double width){
        Window window = mAlertDialog.getWindow();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lParams = window.getAttributes();
        lParams.width = (int) (display.getWidth() *width);
        window.setAttributes(lParams);
        window.setWindowAnimations(R.style.popWindowScaleAnim);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setContentView(view);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    /**
     * 设置弹出配置参数
     * @param context
     * @param mAlertDialog
     * @param view
     */
    public static void setAlertDialogConfigFullScreen(Activity context, AlertDialog mAlertDialog,View view){
        Window window = mAlertDialog.getWindow();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lParams = window.getAttributes();
        lParams.width = display.getWidth();
        lParams.height=display.getHeight();
        window.setAttributes(lParams);
        window.setWindowAnimations(R.style.popWindowScaleAnim);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setContentView(view);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    /**
     * 初始化View视图
     * @param activity
     * @param resourseId
     * @return
     */
   public static View initInflateView(Activity activity,int resourseId){
       return LayoutInflater.from(activity).inflate(resourseId, null);
   }

}
