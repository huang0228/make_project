package com.sam.demo.util;
import android.content.Context;
import android.content.SharedPreferences;

import com.sam.demo.MyApplication;
import com.sam.demo.constants.Data;
import com.sam.demo.constants.Urls;

/**

 * @author WFS
 *
 */
/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/9
 * 描    述： SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用getParam就能获取到保存在手机里面的数据
 * 修订历史：
 * ================================================
 */
public class SharedPreferencesUtils {
	
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String FILE_NAME = "share_date";


	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * @param context
	 * @param key
	 * @param object 
	 */
	public static void setParam(Context context , String key, Object object){
		
		String type = object.getClass().getSimpleName();
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		
		if("String".equals(type)){
			editor.putString(key, (String)object);
		}
		else if("Integer".equals(type)){
			editor.putInt(key, (Integer)object);
		}
		else if("Boolean".equals(type)){
			editor.putBoolean(key, (Boolean)object);
		}
		else if("Float".equals(type)){
			editor.putFloat(type, (Float)object);
		}
		else if("Long".equals(type)){
			editor.putLong(type, (Long)object);
		}
		
		editor.commit();
	}
	
	
	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object getParam(Context context , String key, Object defaultObject){
		String type = defaultObject.getClass().getSimpleName();
		SharedPreferences sp =  MyApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		
		if("String".equals(type)){
			return sp.getString(key, (String)defaultObject);
		}
		else if("Integer".equals(type)){
			return sp.getInt(key, (Integer)defaultObject);
		}
		else if("Boolean".equals(type)){
			return sp.getBoolean(key, (Boolean)defaultObject);
		}
		else if("Float".equals(type)){
			return sp.getFloat(type, (Float)defaultObject);
		}
		else if("Long".equals(type)){
			return sp.getLong(type, (Long)defaultObject);
		}
		return null;
	}
	
	public static void clearData(Context context){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear().commit();
	}
	public static void remove(Context context,String str){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(str).commit();
	}

	//基地址保存
	public static void initBaseUrlInvalidate()
	{
		SharedPreferencesUtils.setParam(MyApplication.getInstance(), Data.initBaseUrl, Urls.SERVER);
	}
}
