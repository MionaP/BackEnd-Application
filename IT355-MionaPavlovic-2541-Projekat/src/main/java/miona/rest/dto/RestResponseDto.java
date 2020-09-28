package miona.rest.dto;

public class RestResponseDto {
	
	private String message;
	private Object data;
	
	public RestResponseDto(String message, Object data) {
		super();
		this.message = message;
		this.data = data;
	}
	
	public RestResponseDto(Object data) {
		this.data = data;
	}
	
	public RestResponseDto(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
