package exception;

public class ControlUnitException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public ControlUnitException() {
		super();
		this.message = "Control Unit error!";
	}
	
	public ControlUnitException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
