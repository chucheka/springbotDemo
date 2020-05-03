package com.neulogic.sendit.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<ParcelErrorResponse> handleAccessDeniedException(Exception ex,WebRequest req){
		ParcelErrorResponse error = new ParcelErrorResponse();
				error.setStatus(HttpStatus.FORBIDDEN.value());
				error.setMessage(ex.getMessage());
				error.setTimeStamp(System.currentTimeMillis());
				return new ResponseEntity<ParcelErrorResponse>(error,HttpStatus.FORBIDDEN);	
						
	}
}
