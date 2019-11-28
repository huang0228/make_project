package com.sam.demo.entity;


/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/2/28
 * 描    述：网络请求返回json基类
 * 修订历史：
 * ================================================
 */
public class BaseResult extends BaseEntity {

	private static final long serialVersionUID = 377559319620277689L;
	private Integer code=0;
	private String msg;



	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "BaseResult{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				'}';
	}
}
