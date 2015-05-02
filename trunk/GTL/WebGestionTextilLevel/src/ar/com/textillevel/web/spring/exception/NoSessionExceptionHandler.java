package ar.com.textillevel.web.spring.exception;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoSessionExceptionHandler {

	@ExceptionHandler({ NoSessionException.class })
	protected void handleInvalidRequest(Exception e, HttpServletResponse resp) throws Exception {
		resp.sendError(403, "La sesión ha expirado");
	}
}