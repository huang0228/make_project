package com.sam.demo.constants;

/**
 * Created by lenovo on 2018/3/16.
 */

public class Urls {

    public static final  String LOCAL_HTML_PATH = "file:///android_asset/H5/page/";


    public static final String SERVER = "http://192.168.34.234:8081";//科技公司

    /**
     * 登录
     */
    public static final String LOGIN = SERVER + "/api/user/Login";
    /**
     * 切换登录账号
     */
    public static final String SwitchUser = SERVER + "/api/user/SwitchUser";

    /**
     * 获取TOKEN
     */
    public static final String GET_TOKEN = SERVER + "/api/token/GetToken";


    /**
     * 获取Hash值
     */
    public static final String GET_HASH_MD5 = SERVER + "/api/user/ValidateApp";

}
