package exception;

public class RegistersException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public RegistersException() {
		super();
		this.message = "Control Unit error!";
	}
	
	public RegistersException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
