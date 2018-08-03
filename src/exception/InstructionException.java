package exception;

public class InstructionException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public InstructionException() {
		super();
		this.message = "Instruction error!";
	}
	
	public InstructionException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
