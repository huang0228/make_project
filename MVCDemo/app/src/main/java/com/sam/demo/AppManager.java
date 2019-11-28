package com.sam.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;


/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/2/1
 * 描    述：应用程序Activity管理类：用于Activity管理和应用程序退出
 * 修订历史：
 * ================================================
 */
public class AppManager {
	
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	
	private AppManager(){}
	/**
	 * 单一实例
	 */
	public static AppManager getAppManager(){
		if(instance==null){
			instance=new AppManager();
		}
		return instance;
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=null;
		try {
			if (activityStack==null||activityStack.size()==0){
				return activity=new Activity();
			}
			activity=activityStack.lastElement();
		}catch (Exception e){
			return activity=new Activity();
		}
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		if (activityStack==null||activityStack.size()==0){
			return;
		}
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}

	/**
	 * 判断某activity是否在占顶
	 */
	public boolean hasActivityExistTop(Class<?> cls){
		boolean res=false;
		if (activityStack.size()>0){
			Activity activity=activityStack.lastElement();
			if(activity.getClass().equals(cls) ){
				res=true;
			}
			return res;
		}else{
			return res;
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		if (activityStack==null||activityStack.size()==0){
			return;
		}
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(!activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 退出应用程序
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {

		}
	}
}