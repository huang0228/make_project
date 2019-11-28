/**
 * 
 */
package com.sam.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/5/9
 * 描    述：正方式LinearLayout
 * 修订历史：
 * ================================================
 */

public class SquareLinearLayout extends LinearLayout{
	public SquareLinearLayout(Context context) {
        super(context);
    }
 
    public SquareLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
     
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
     
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
