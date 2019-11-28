package com.sam.demo.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author 赵腾飞
 * @date 2016年5月19日16:11:10
 */

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/2/28
 * 描    述：实体类基类 json格式的字符串和实体类相互转换
 * 修订历史：
 * ================================================
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -3941147818153056738L;
    public static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    /**
     * 转换为字符串
     * @return JsonString
     */
    public String toJson(){
        return gson.toJson(this);
    }


    /**
     *
     * @param jsonString
     * @return
     */
    public <T extends BaseEntity> T fromJson(String jsonString){

        return (T)gson.fromJson(jsonString, this.getClass());
    }

    /**
     *
     * @param jsonString
     * @return
     */
    public <T extends BaseEntity> T toEntity(String jsonString){
        try{
            return fromJson(jsonString);

        }catch (Exception e){
            BaseResult baseResult=gson.fromJson(jsonString,BaseResult.class);
            return fromJson(baseResult.toJson());
        }
    }
    /**
     * 将实体类的List转换成json字符串
     * @param list  需要转换的实体类List
     * @return
     * @throws
     */
    public static <E> String getJsonStringByList(List<E> list) {
        StringBuilder strJson = new StringBuilder("[");
        Gson gson = new Gson();
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                strJson.append(gson.toJson(list.get(i)) + ",");
            } else {
                strJson.append(gson.toJson(list.get(i)));
            }
        }
        strJson = strJson.append("]");
        return strJson.toString();
    }

}
