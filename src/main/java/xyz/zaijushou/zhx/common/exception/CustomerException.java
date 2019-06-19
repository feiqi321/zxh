package xyz.zaijushou.zhx.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class CustomerException extends RuntimeException implements ErrorInfoInterface {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Integer code;

	private String msg;

	public CustomerException(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
		logger.error(msg);
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}
}
