package com.sam.demo.entity;
/**
 * ================================================
 * 版    本：1.0.1
 * 创建日期：2018/3/14
 * 描    述：请求头未加密参数
 * 修订历史：
 * ================================================
 */

public class Header extends BaseEntity{
    private static final long serialVersionUID = -12553120034579626L;
    private String APP_KEY;
    private String USER_IDENTITY;
    private String TOKEN;
    private String SIGNATURE;
    private Long TIME_STAMP;


    public String getSIGNATURE() {
        return SIGNATURE;
    }

    public void setSIGNATURE(String SIGNATURE) {
        this.SIGNATURE = SIGNATURE;
    }

    public String getAPP_KEY() {
        return APP_KEY;
    }

    public void setAPP_KEY(String APP_KEY) {
        this.APP_KEY = APP_KEY;
    }

    public String getUSER_IDENTITY() {
        return USER_IDENTITY;
    }

    public void setUSER_IDENTITY(String USER_IDENTITY) {
        this.USER_IDENTITY = USER_IDENTITY;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public Long getTIME_STAM() {
        return TIME_STAMP;
    }

    public void setTIME_STAM(Long TIME_STAM) {
        this.TIME_STAMP = TIME_STAM;
    }

    @Override
    public String toString() {
        return "Header{" +
                "APP_KEY='" + APP_KEY + '\'' +
                ", USER_IDENTITY='" + USER_IDENTITY + '\'' +
                ", TOKEN='" + TOKEN + '\'' +
                ", SIGNATURE='" + SIGNATURE + '\'' +
                ", TIME_STAMP=" + TIME_STAMP +
                '}';
    }
}
