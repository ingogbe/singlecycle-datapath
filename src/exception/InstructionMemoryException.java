package exception;

public class InstructionMemoryException extends Exception{
	private static final long serialVersionUID = 1L;
	private String message;
	
	public InstructionMemoryException() {
		super();
		this.message = "Instruction Memory error!";
	}
	
	public InstructionMemoryException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
}
