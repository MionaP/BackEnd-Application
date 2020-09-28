package miona.customexceptions;

public class ValidationException extends RuntimeException {
	
	
	private static final long serialVersionUID = 927669603815683058L;
	
	private String error;
	private Object data;
	
	public ValidationException() {
		
	}
	
	public ValidationException(String error) {
		super();
		this.error = error;
	}

	public ValidationException(String error, Object data) {
		super();
		this.error = error;
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
