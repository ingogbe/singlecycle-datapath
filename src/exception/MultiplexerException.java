package exception;

public class MultiplexerException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public MultiplexerException() {
		super();
		this.message = "Instruction error!";
	}
	
	public MultiplexerException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
