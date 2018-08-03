package exception;

public class BitDataException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public BitDataException() {
		super();
		this.message = "Bit data error!";
	}
	
	public BitDataException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
