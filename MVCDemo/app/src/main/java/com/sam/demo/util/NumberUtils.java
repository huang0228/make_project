package com.sam.demo.util;

/**
 * 作者：sam.huang
 * <p>数字转换类
 * 创建日期：2019/4/2
 * <p>
 * 描述：
 */
public class NumberUtils {

    public static String showNumber(String input)
    {
        int temp=0;
        if (StringUtils.isEmpty(input))
        {
            return "0";
        }else
        {
            try {
                temp=Integer.parseInt(input);
                if (temp>99)
                {
                    return "99+";
                }else
                {
                    return input;
                }
            }catch (Exception e)
            {
                //捕获非数字格式内容时
                return "0";
            }

        }
    }


    public static String showNumber(int input)
    {
        if (input==0)
        {
            return "0";
        }else
        {
            if (input>99)
            {
                return "99+";
            }else
            {
                return String.valueOf(input);
            }
        }
    }
}
