package com.sam.demo.entity;

import java.io.Serializable;

/**
 * 作者：sam.huang
 * <p>本地变量
 * 创建日期：2019/10/30
 * <p>
 * 描述：
 */
public class LocalMode implements Serializable {
    private String Instrument_Type;
    private String Business_Type;
    private String Module_Id;

    public String getInstrument_Type() {
        return Instrument_Type;
    }

    public void setInstrument_Type(String instrument_Type) {
        Instrument_Type = instrument_Type;
    }

    public String getBusiness_Type() {
        return Business_Type;
    }

    public void setBusiness_Type(String business_Type) {
        Business_Type = business_Type;
    }

    public String getModule_Id() {
        return Module_Id;
    }

    public void setModule_Id(String module_Id) {
        Module_Id = module_Id;
    }
}
