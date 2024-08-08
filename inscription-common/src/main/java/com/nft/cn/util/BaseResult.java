package com.nft.cn.util;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class BaseResult<T> implements Serializable {

	private int code;
	private String msg;
	private T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public BaseResult() {
	}

	public BaseResult(Type type, String msg) {
		this.code = type.value;
		this.msg = msg;
	}

	public BaseResult(Type type, String msg, T data) {
		this.code = type.value;
		this.msg = msg;
		if (data != null) {
			this.data = data;
		}

	}
	public static <T> BaseResult<T> success() {
		return success("Successful operation",null);
	}

	public  static <T> BaseResult<T> success(T data) {
		return success("Successful operation", data);
	}


	public static <T> BaseResult<T> success(String msg, T data) {
		return new BaseResult<T>(Type.SUCCESS, msg, data);
	}


	public static <T> BaseResult<T> use(String msg, T data) {
		return new BaseResult<T>(Type.USE, msg, data);
	}


	public static <T> BaseResult<T> successMsg(String msg) {
		return new BaseResult<T>(Type.SUCCESS, msg, null);
	}
	public static <T> BaseResult<T> warn(String msg) {
		return warn(msg, (T) null);
	}

	public static <T> BaseResult<T> warn(String msg, T data) {
		return new BaseResult<T>(Type.WARN, msg, data);
	}


	public static <T> BaseResult<T> unAuth(String msg) {
		return new BaseResult<T>(Type.UNAUTH, msg, (T)null);
	}

	public static <T> BaseResult<T> error() {
		return fail("operation failed");
	}

	public static <T> BaseResult<T> fail(String msg) {
		return error(msg, (T)null);
	}


	public static <T> BaseResult<T> fail(String msg, T data) {
		return error(msg,data);
	}

	public static <T> BaseResult<T> error(String msg, T data) {
		return new BaseResult<T>(Type.ERROR, msg, data);
	}

	public static enum Type {
		SUCCESS(200),
		WARN(402),
		UNAUTH(401),
		USE(525),
		ERROR(500);

		private final int value;

		private Type(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("code", code)
				.append("msg", msg)
				.append("data", data)
				.toString();
	}
}
