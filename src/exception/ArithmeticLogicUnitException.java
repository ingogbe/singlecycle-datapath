package exception;

public class ArithmeticLogicUnitException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public ArithmeticLogicUnitException() {
		super();
		this.message = "File error!";
	}
	
	public ArithmeticLogicUnitException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
