package com.sam.demo.util;

import android.widget.EditText;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2018/9/17
 * <p>
 * 描述：ditText竖直方向是否可以滚动
 */
public class EditTextUtils {

    public static boolean  canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

}
