package com.sam.demo.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.entity.User;
import com.sam.demo.widget.WaterMarkDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2019/2/1
 * <p>
 * 描述：水印帮助类
 */
public class WaterMarkUtil {

    /**
     * 显示水印布局
     *
     * @param activity
     */
    public static boolean showWatermarkView(final Activity activity) {
        ViewGroup rootView = getRootView(activity);
        View framView = LayoutInflater.from(activity).inflate(R.layout.layout_water_mark, null);
        View waterRoot=framView.findViewById(R.id.li_water_root);
        List<String> labels = new ArrayList<>();
        labels.add(getUser());
        waterRoot.setBackground(new WaterMarkDrawable(activity,labels,-30,13));
        if (rootView.getChildCount()<2)//防止多次添加水印
        {
            rootView.addView(framView);
        }
        return true;
    }

    //查找布局的底层
    protected static ViewGroup getRootView(Activity context)
    {
        return (ViewGroup)context.findViewById(android.R.id.content);
    }

    public static String getUser()
    {
        User user = MyApplication.getInstance().getUser();
        if (user==null)
        {
            return MyApplication.getInstance().getResources().getString(R.string.app_name);
        }else
        {
            return user.getLOGIN_NAME()==null?MyApplication.getInstance().getResources().getString(R.string.app_name):
                    user.getLOCAL_NAME()+"("+user.getLOGIN_NAME()+")  "+MyApplication.getInstance().getResources().getString(R.string.app_name_water_mark);
        }
    }

}
