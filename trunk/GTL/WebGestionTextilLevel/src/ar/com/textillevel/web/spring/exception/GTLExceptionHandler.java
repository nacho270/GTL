package ar.com.textillevel.web.spring.exception;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GTLExceptionHandler {

	@ExceptionHandler({ GTLException.class })
	protected void handleInvalidRequest(Exception e, HttpServletResponse resp) throws Exception {
		GTLException gtle = (GTLException) e;
		resp.sendError(gtle.getCodigo(), e.getMessage());
	}
}