package exception;

public class BarrelExtensorException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public BarrelExtensorException() {
		super();
		this.message = "Instruction error!";
	}
	
	public BarrelExtensorException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
