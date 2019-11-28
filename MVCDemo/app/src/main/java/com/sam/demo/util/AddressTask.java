package com.sam.demo.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import cn.addapp.pickers.entity.Province;
import cn.addapp.pickers.util.ConvertUtils;


/**
 * 获取地址数据
 * 2019.10.29
 */
public class AddressTask extends AsyncTask<String, Void, ArrayList<Province>> {
    private Activity activity;
    private ProgressDialog dialog;
    private Callback callback;

    public AddressTask(Activity activity) {
        this.activity = activity;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, null, "加载中...", true, true);
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        ArrayList<Province> data = new ArrayList<>();
        try {
            String json = ConvertUtils.toString(activity.getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json, Province.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> result) {
        dialog.dismiss();
        if (result.size() > 0) {
            callback.onAddressInitSuccess(result);
        } else {
            callback.onAddressInitFailed("解析失败");
        }
    }

    public interface Callback{

        void onAddressInitFailed(String error);
        void onAddressInitSuccess(ArrayList<Province> provinces);
    }

}
