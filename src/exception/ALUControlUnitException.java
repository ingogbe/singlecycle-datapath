package exception;

public class ALUControlUnitException extends Exception{
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ALUControlUnitException() {
		super();
		this.message = "Instruction error!";
	}
	
	public ALUControlUnitException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
}
