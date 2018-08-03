package exception;

public class DataMemoryException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public DataMemoryException() {
		super();
		this.message = "Instruction error!";
	}
	
	public DataMemoryException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
