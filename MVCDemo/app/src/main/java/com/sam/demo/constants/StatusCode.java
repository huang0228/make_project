package com.sam.demo.constants;

/**
 */

public class StatusCode {
    // 操作失败
    public static int ERROR = -1;
    // 操作成功
    public static int SUCCESS = 0;
    // 操作成功一半
    public static int SUCCESS_ERROR = 1;
    // 请求头错误
    public static String REQUEST_HEADER_ERROR = "-201";

    // Token失效
    public static String TOKEN_INVALID = "-202";
    // 签名验证不通过
    public static String SIGNATURE_INVALID = "-203";
    // 系统异常
    public static String SERVER_ERROR = "-204";
    // 身份验证不通过
    public static int PASSPORT_ERROR = 401;
    //
    public static int REQUEST_SUCCESS = 200;

    //密码被修改
    public static String PASS_RESET ="-3006";
    //用户已经锁定或已删除
    public static String USER_DEL_OR_LOCK="-3007";
    //用户解除绑定
    public static String USER_DEVICE_UNLOCK="-3008";
}
