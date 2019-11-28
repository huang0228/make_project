package com.sam.demo.util;

import android.content.Intent;
import android.os.Handler;
import android.util.Base64;

import com.sam.demo.AppManager;
import com.sam.demo.MyApplication;
import com.sam.demo.R;
import com.sam.demo.base.BaseActivity;
import com.sam.demo.base.LoginActivity;
import com.sam.demo.constants.Common;
import com.sam.demo.constants.HttpMethods;
import com.sam.demo.constants.StatusCode;
import com.sam.demo.entity.BaseResult;
import com.sam.demo.entity.Header;
import com.sam.demo.entity.User;
import com.sam.demo.base.BaseFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 网络请求回调
 *
 * @author 赵腾飞
 * @modify 牛世杰
 * @date 2017年4月18日18:09:11
 */

public class MyStringCallback extends StringCallback {
    // 请求结果监听
    private RequestResultListener requestResultListener;
    // 上下文
    private BaseActivity context;
    public int flag = 0;
    // 是否显示进度框
    public boolean showProgress = true;//是否弹出加载框
    // 当前用户
    public User user = MyApplication.getInstance().getUser();
    //当前请求
    private Request currentRequest;

    public MyStringCallback(MyStringCallback.RequestResultListener requestResultListener, Boolean showProgress) {
        super();
        this.context = (BaseActivity) AppManager.getAppManager().currentActivity();
        this.requestResultListener = requestResultListener;
        this.showProgress = showProgress;
    }

    public MyStringCallback(BaseActivity context, int flag) {
        super();
        this.context = context;
        this.flag = flag;

        try {
            requestResultListener = (RequestResultListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " Must implent RequestResultListener");
        }
    }

    public MyStringCallback(BaseActivity context, int flag, boolean showProgressDialog) {
        this(context, flag);
        showProgress = showProgressDialog;

    }

    public MyStringCallback(BaseActivity context, int flag, boolean showProgressDialog, RequestResultListener sr) {
        this.requestResultListener = sr;
        this.context = context;
        this.flag = flag;
        this.showProgress = showProgressDialog;
    }

    public MyStringCallback(BaseFragment context, int flag) {
        super();
        this.context = (BaseActivity) context.getActivity();
        this.flag = flag;
        try {
            requestResultListener = (RequestResultListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " Must implent RequestResultListener");
        }
    }

    public MyStringCallback(BaseFragment context, int flag, boolean showProgressDialog) {
        this(context, flag);
        showProgress = showProgressDialog;
    }


    // 重新发起请求
    private void retry() {
        Request request = null;
        switch (currentRequest.getMethod().toString()) {
            case HttpMethods.GET:
                request = new GetRequest(currentRequest.getUrl());
                break;
            case HttpMethods.POST:
                request = new PostRequest(currentRequest.getUrl());
                break;
        }
        request.tag(currentRequest.getTag());
        for (ConcurrentHashMap.Entry<String, List<String>> entry : currentRequest.getParams().urlParamsMap.entrySet()) {
            String value = "";
            if (!ListUtils.isEmpty(entry.getValue()))
                value = entry.getValue().get(0);

            request.params(entry.getKey(), value);
        }
        request.execute(MyStringCallback.this);
    }

    // 设置请求头
    private void setHeader(Request request) {
        Header header = new Header();
        Long time_stam = TimeUtils.getGMT8TimeTmplByGMT0(Common.SERVER_TIMES_TAMP);
        header.setAPP_KEY(user == null ? "" : user.getAPP_KEY());
        header.setTOKEN(TokenManager.getToken().getValue());
        header.setTIME_STAM(time_stam);
        header.setUSER_IDENTITY(user == null ? "" : user.getUSER_GUID());
        header.setSIGNATURE(MD5Utils.encode((user == null ? "" : user.getAPP_KEY())
                + (time_stam)
                + (user == null ? "" : user.getAPP_SECRET())));
        request.headers("Authorization",
                "Basic " + Base64.encodeToString(header.toJson().replaceAll("\n", "").replaceAll(" ", "").getBytes(),
                        Base64.DEFAULT).replace("\n", ""));
    }

    // 请求成功回调
    @Override
    public void onSuccess(final Response<String> response) {
        Common.SERVER_TIMES_TAMP = response.headers().getDate("Date").getTime();

        String jsonStr = response.body().toString();
        MyLog.d("jsonStr", jsonStr);
        if (response.code() == StatusCode.PASSPORT_ERROR) {
            /**
             * -201：请求头错误
             * -202：token失效
             *
             * -203：身份或签名验证不通过；
             * -204：服务器异常
             */
            BaseResult result = new BaseResult();
            result.setCode(StatusCode.ERROR);
            if (jsonStr.equals(StatusCode.REQUEST_HEADER_ERROR)) {
                result.setMsg("系统错误，请联系管理员");
                requestResultListener.onError(result.toJson(), flag);
            } else if (jsonStr.equals(StatusCode.TOKEN_INVALID)) {
                TokenManager.requestToken(new TokenManager.GetTokenListener() {

                    @Override
                    public void afterGetToken() {
                        retry();
                    }
                });
            } else if (jsonStr.equals(StatusCode.SIGNATURE_INVALID)) {
                result.setMsg("系统错误，请联系管理员");
                requestResultListener.onError(result.toJson(), flag);
            } else if (jsonStr.equals(StatusCode.SERVER_ERROR)) {
                result.setMsg("服务器异常，请联系管理员");
                requestResultListener.onError(result.toJson(), flag);
            } else if (jsonStr.equals(StatusCode.PASS_RESET)) {
                showToastSleep(MyApplication.getInstance().getResources().getString(R.string.pwd_modify_already));
            } else if (jsonStr.equals(StatusCode.USER_DEL_OR_LOCK)) {
                showToastSleep(MyApplication.getInstance().getResources().getString(R.string.user_locked_or_del));
            } else if (jsonStr.equals(StatusCode.USER_DEVICE_UNLOCK)) {
                showToastSleep(MyApplication.getInstance().getResources().getString(R.string.user_device_unlock));
            } else {
                //401 - 未授权: 由于凭据无效，访问被拒绝
                result.setMsg("服务器异常");
                requestResultListener.onError(result.toJson(), flag);
            }

        } else if (response.code() == StatusCode.REQUEST_SUCCESS) {
            requestResultListener.onSuccess(jsonStr, flag);
        }
    }

    // 请求前回调
    @Override
    public void onStart(final Request<String, ? extends Request> request) {
        super.onStart(request);
        currentRequest = request;
        //currentRequest.getParams().put("MENU_QUERYPERSONAL",0);
        //判断是否有网络，无网络取消掉所有请求请求
        if (!Utils.isNetworkConnected(context)) {
            //取消掉所有请求
//            OkGo.getInstance().cancelAll();
            requestResultListener.onError(MyApplication.getInstance().getResources().getString(R.string.net_connection_error), flag);

        } else if (showProgress) {
            /**add by sam.huang
             * 增加异常捕获，避免报 Unable to add window -- token android.os.BinderProxy@f803856 is not valid; is your activity running?
             * 窗口所依附的父页面不存在了
             */
            try {
                context.showProgressDialog("请稍后");
            } catch (Exception e) {

            }

        }
        /* 主要用于在所有请求之前添加公共的请求头或请求参数
         例如登录授权的 token
         使用的设备信息
         可以随意添加,也可以什么都不传
         还可以在这里对所有的参数进行加密，均在这里实现*/
        // 需token
        if (!request.getParams().urlParamsMap.containsKey("noToken")) {
            if (TokenManager.getToken() != null) {
                setHeader(request);
            } else {
                OkGo.getInstance().cancelTag(request.getTag());
                TokenManager.requestToken(new TokenManager.GetTokenListener() {
                    @Override
                    public void afterGetToken() {
                        retry();
                    }
                });
            }
        }
        MyLog.d("https", request.getParams().urlParamsMap.toString());

    }

    // 请求失败回调
    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        String a = response.getException().getMessage();
        MyLog.d("sam-->",a);
        if (!Utils.isNetworkConnected(context)) {
            requestResultListener.onError("网络无连接，请检查网络", flag);
            ToastUtil.showToast(context, "网络无连接，请检查网络");
        } else {
            requestResultListener.onError("网络或服务器异常", flag);
            ToastUtil.showToast(context, "网络或服务器异常");
        }
    }

    // 请求完成回调
    @Override
    public void onFinish() {
        super.onFinish();
        try {
            if (showProgress) {
                context.cancelProgressDialog();
            }
        } catch (Exception e) {
        }

    }

    public interface RequestResultListener {
        void onSuccess(String str, int flag);

        void onError(String str, int flag);
    }

    private void showToastSleep(String toastMsg) {
        ToastUtil.showToast(context, toastMsg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!AppManager.getAppManager().hasActivityExistTop(LoginActivity.class))
                {
                    AppManager.getAppManager().finishAllActivity();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);
                }
            }
        }, 1500);
    }

}
