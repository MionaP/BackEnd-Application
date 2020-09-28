package miona.customexceptions;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import miona.rest.dto.RestResponseDto;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
	
	
	/**
	 * Method for handle {@link ValidationException}
	 * @param ex
	 * @return 
	 */
	@ExceptionHandler(ValidationException.class)
	protected ResponseEntity<Object> handleBussinessValidationException(ValidationException ex) {

		RestResponseDto restMessageDto = new RestResponseDto(ex.getError(), ex.getData());
			
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
		headers.setContentType(mediaType);
		
		return new ResponseEntity<>(restMessageDto, headers, httpStatus);
	}

}
