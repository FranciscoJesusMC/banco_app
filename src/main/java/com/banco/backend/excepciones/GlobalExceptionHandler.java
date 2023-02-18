package com.banco.backend.excepciones;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.banco.backend.dto.ErrorDetalles;

@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetalles> manejarResource(ResourceNotFoundException exception,WebRequest request){
		ErrorDetalles error = new ErrorDetalles(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
			
	}
	
	@ExceptionHandler(BancoAppException.class)
	public ResponseEntity<ErrorDetalles> manejarBancoAppException(BancoAppException exception, WebRequest request){
		ErrorDetalles error = new ErrorDetalles(new Date(), exception.getMensaje(), request.getDescription(false));
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetalles> manejarException(Exception exception, WebRequest request){
		ErrorDetalles error = new ErrorDetalles(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errores = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error)->{
		String nombreDelCampo = ((FieldError)error).getField();
		String mensaje = error.getDefaultMessage();
		
		errores.put(nombreDelCampo, mensaje);
		
	});
		return new ResponseEntity<>(errores,HttpStatus.BAD_REQUEST);
	}
			


}
