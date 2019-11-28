package com.sam.demo.util;

import com.sam.demo.AppManager;
import com.sam.demo.MyApplication;
import com.sam.demo.base.BaseActivity;
import com.sam.demo.constants.Flag;
import com.sam.demo.constants.StatusCode;
import com.sam.demo.constants.Urls;
import com.sam.demo.entity.Token;
import com.sam.demo.entity.TokenResult;
import com.sam.demo.entity.User;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/4/11.
 */

public class TokenManager {
    private static Token token;
    public static int timer;
    private static boolean isCalling = false;
    private static List<GetTokenListener> listeners = new ArrayList<>();

    //add by sam.huang 2018.09.13

    public  static void resetCalling()
    {
        isCalling=false;
    }

    public static Token getToken() {
        return token;
    }
    public static void clearToken() {
        token = null;
    }

    public static void requestToken(final GetTokenListener listener) {
        if(listener != null){
            listeners.add(listener);
        }
        if (!isCalling) {
            isCalling = true;
            User user = MyApplication.getInstance().getUser();
            OkGo.<String>get(Urls.GET_TOKEN)
                    .params("appKey", user.getAPP_KEY())
                    .params("appSecret", user.getAPP_SECRET())
                    .params("noToken", true)
                    .execute(new MyStringCallback((BaseActivity) AppManager.getAppManager().currentActivity(),
                            Flag.MINUS_ONE, false, new MyStringCallback.RequestResultListener() {
                        @Override
                        public void onSuccess(String str, int flag) {
                            TokenResult tokenResult = new TokenResult().fromJson(str);
                            if (tokenResult.getCode() == StatusCode.SUCCESS) {
                                token = tokenResult.getData();
                                timer = token.getDuration();
                                isCalling = false;
                            }
                            for(GetTokenListener ler : listeners){
                                ler.afterGetToken();
                            }
                            listeners.clear();
                        }

                        @Override
                        public void onError(String str, int flag) {
                            isCalling = false;
                        }
                    }));
        }

    }

    public interface GetTokenListener {
        void afterGetToken();
    }
}
