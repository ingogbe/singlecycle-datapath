package exception;

public class BarrelShifterException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public BarrelShifterException() {
		super();
		this.message = "Instruction error!";
	}
	
	public BarrelShifterException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
