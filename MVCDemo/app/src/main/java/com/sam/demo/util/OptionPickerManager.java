package com.sam.demo.util;

import android.app.Activity;
import android.graphics.Color;

import java.util.List;

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * Created by Administrator on 2018/5/17.
 */

public class OptionPickerManager  {
    public static <T> SinglePicker<T>  onOptionPicker(Activity context, List<T> list, OnItemPickListener listener) {

        final SinglePicker<T> picker = new SinglePicker<>(context, list);
        picker.setCanLoop(false);//不禁用循环
        picker.setWheelModeEnable(true);
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setWeightWidth(1);
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int i, String s) {
                picker.setTitleText(s);
            }
        });

        picker.setOnItemPickListener(listener);
        return picker;
    }
}
