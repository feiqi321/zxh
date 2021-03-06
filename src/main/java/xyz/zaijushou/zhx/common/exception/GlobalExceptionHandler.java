package xyz.zaijushou.zhx.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.zaijushou.zhx.common.web.WebResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(value = IllegalArgumentException.class)
	public WebResponse errorHandlerIll(HttpServletRequest request, IllegalArgumentException exception) {
		WebResponse result = WebResponse.buildResponse();
		logger.error("出现异常:{}",exception);
		result.setCode("500");
		result.setMsg(exception.getMessage());
		exception.printStackTrace();
		return result;
	}

	@ExceptionHandler(value = CustomerException.class)
	public WebResponse errorHandlerOverJson(HttpServletRequest request, CustomerException exception) {
		WebResponse result = WebResponse.buildResponse();
		logger.error("出现异常:{}",exception);
		result.setCode("500");
		result.setMsg(exception.getMsg());
		exception.printStackTrace();
		return result;
	}

	@ExceptionHandler(value = Exception.class)
	public WebResponse handleException(Exception exception) {
		WebResponse result = WebResponse.buildResponse();
		logger.error("出现异常:{}",exception);
		exception.printStackTrace();
		result.setCode("500");
		result.setMsg("后台异常，请稍后再试");
		return result;
	}


}
