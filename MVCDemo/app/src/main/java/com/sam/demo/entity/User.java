package com.sam.demo.entity;

import android.util.Base64;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/2/28
 * 描    述：User信息
 * 修订历史：
 * ================================================
 */

public class User extends BaseEntity {


    private static final long serialVersionUID = 2576271958050683695L;
    private String USER_GUID;

    private String LOCAL_NAME;
    private String DISPLAY_POST;

    private String LOGIN_NAME;

    private String ENGLISH_NAME;

    private String ADDRESS;

    private String LOGIN_TIME;

    private String POST_NAME;

    private int ISLOCKED;

    private String HEADIMGURL;
    private String DP_NAME;

    private String AREA_NAME;

    private String APP_KEY;

    private String APP_SECRET;

    private String MOBILE;

    private String NICKNAME;

    private String EMAIL;

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public void setLOCAL_NAME(String LOCAL_NAME) {
        this.LOCAL_NAME = LOCAL_NAME;
    }

    public String getDISPLAY_POST() {
        return DISPLAY_POST;
    }

    public void setDISPLAY_POST(String DISPLAY_POST) {
        this.DISPLAY_POST = DISPLAY_POST;
    }

    public String getDP_NAME() {
        return DP_NAME;
    }

    public void setDP_NAME(String DP_NAME) {
        this.DP_NAME = DP_NAME;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUSER_GUID() {
        return USER_GUID;
    }

    public String getLOCAL_NAME() {
        return LOCAL_NAME;
    }

    public String getLOGIN_NAME() {
        return LOGIN_NAME;
    }

    public void setLOGIN_NAME(String LOGIN_NAME) {
        this.LOGIN_NAME = LOGIN_NAME;
    }

    public String getENGLISH_NAME() {
        return ENGLISH_NAME;
    }

    public void setENGLISH_NAME(String ENGLISH_NAME) {
        this.ENGLISH_NAME = ENGLISH_NAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getLOGIN_TIME() {
        return LOGIN_TIME;
    }

    public void setLOGIN_TIME(String LOGIN_TIME) {
        this.LOGIN_TIME = LOGIN_TIME;
    }

    public String getPOST_NAME() {
        return POST_NAME;
    }

    public void setPOST_NAME(String POST_NAME) {
        this.POST_NAME = POST_NAME;
    }

    public int getISLOCKED() {
        return ISLOCKED;
    }

    public String getHEADIMGURL() {
        return HEADIMGURL;
    }

    public void setHEADIMGURL(String HEADIMGURL) {
        this.HEADIMGURL = HEADIMGURL;
    }

    public String getAREA_NAME() {
        return AREA_NAME;
    }

    public void setAREA_NAME(String AREA_NAME) {
        this.AREA_NAME = AREA_NAME;
    }

    public String getAPP_KEY() {
        try {
            return new String(Base64.decode(APP_KEY, Base64.DEFAULT));
        }catch (Exception e)
        {
            //APP_KEY为null时捕获异常
            return "";
        }
    }

    public String getAPP_SECRET() {
        try {
            return new String(Base64.decode(APP_SECRET, Base64.DEFAULT));
        }catch (Exception e)
        {
            //APP_SECRET为null时捕获异常
            return "";
        }

    }

}
