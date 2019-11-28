package com.sam.demo;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationManagerCompat;

import com.sam.demo.constants.Data;
import com.sam.demo.entity.BaseEntity;
import com.sam.demo.entity.User;
import com.sam.demo.util.FileUtil;
import com.sam.demo.util.MyLog;
import com.sam.demo.util.SharedPreferencesUtils;
import com.sam.demo.widget.refreshLayout.ClassicFooter;
import com.sam.demo.widget.refreshLayout.ClassicHeader;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.greenrobot.eventbus.EventBus;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import me.dkzwm.widget.srl.IRefreshViewCreator;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.OkHttpClient;

import static com.sam.demo.constants.Data.unReadTotal;

/**
 * ================================================
 * <p>
 * 创建日期：2018/2/1
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication myapp;
    public static Context AppContent;

    public static boolean isDevelop=true;//是否是开发模式

    private User user = null;

    public static List<String> getAuth(){
       String str= SharedPreferencesUtils.getParam(myapp, Data.auths, "").toString();
       return Arrays.asList(str.split(","));
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myapp = this;
        AppContent=getApplicationContext();
        ShortcutBadger.removeCount(myapp);
        initOkGo();
        initSmoothRefreshLayout();
    }

    // 获取application实例
    public static MyApplication getInstance() {
        return myapp;
    }

    /**
     * 静态添加下拉刷新，上拉加载布局
     */
    public void initSmoothRefreshLayout() {
        SmoothRefreshLayout.setDefaultCreator(new IRefreshViewCreator() {
            @Override
            public void createHeader(SmoothRefreshLayout layout) {
                ClassicHeader header = new ClassicHeader(layout.getContext());
                layout.setHeaderView(header);
            }

            @Override
            public void createFooter(SmoothRefreshLayout layout) {
                ClassicFooter footer = new ClassicFooter(layout.getContext());
                layout.setFooterView(footer);
            }
        });


    }

    /**
     * 获取保存到文件的user数据
     *
     * @return
     */
    public User getUser() {

        user = (User) FileUtil.readObject(getApplicationContext(), Data.USER);
        if(user==null)
        {
            user=new User();
        }
        return user;
    }

    /**
     * 初始化OkGo
     */
    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志

        //超时时间设置，默认30秒
        builder.readTimeout(30000, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(30000, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(30000, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置
        //方法一：信任所有证书
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);

        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0);                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0

    }
}
