package com.sam.demo.constants;

/**
 * Created by lenovo on 2018/3/16.
 */

public class Data {

    public static final String initPwd="Yqyf123";
    public static final String initBaseUrl="baseUrl";
    public static final String unReadTotal="unReadTotal";//未读消息参数

    public static final int minYearForReport=2016;//2016年报表通用标准
    public static final int maxYearForReport=0;//报表通用标准当前时间后置弹性N年参数
    public static final int minYear=1999;//1999年，本应用通用标准
    public static final int maxYear=30;//当前时间后置弹性N年参数 编辑页面使用

    /**
     * 保存token的文件名
     */
    public static final String TOKEN="token";
    /**
     * 保存是否登录的sp名
     */
    public static final String ISLOGIN="isLogin";
    /**
     * 保存user的文件名
     */
    public static final String USER="user";

    /**
     * 保存用户权限的所有信息
     */
    public static final String baseAuthInfo="baseAuthInfo";

    public static final String FIRST_OPEN="first_open1.0.2";
    public static final String TOP_ID="top_id";
    public static final String SEARCH_ORGANIZATION_NAME="search_organization_name";
    public static final String DEVICE_TOKEN="deviceToken";
    public static final String auths="auths";

    public static final String cutMenu="cut_menu";
    public static final String sortMenu="sort_menu";
    public static final String tempAuths="temp_auths";


    public static final String tempBlockAllAuthCode="temp_block_auths_code";
    public static final String tempBlockAllAuthName="temp_block_auths_name";
    public static final String blockMenu="block_menu";
    public static final String blockSortMenu="block_sort_menu";


    public static final String cacheTimeForTest="cacheTimeForTest";
    public static final String cacheTimeForMap="cacheTimeForMap";
    public static final String cacheDataForTest="cacheDataForTest";
    public static final String cacheDataForMap="cacheDataForMap";
    public static final String cacheDataForMapDetail="cacheDataForMapDetail";

    public static final String PICTURE_EXPRESSION = "(png|jpeg|gif|jpg|bmp)";
    public static final String EXECL_EXPRESSION = "(xls|xlsx|xlsm|xltx|xltm|xlsb|xlam|csv)";
    public static final String PDF_EXPRESSION = "(pdf)";
    public static final String PPT_EXPRESSION = "(ppt|pptx|pptm|ppsx|ppsx|potx|potm|ppam)";
    public static final String WORD_EXPRESSION = "(doc|docx|docm|dotx|dotm)";
    public static final String ZIP_EXPRESSION = "(zip|rar)";
}
