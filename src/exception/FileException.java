package exception;

public class FileException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public FileException() {
		super();
		this.message = "File error!";
	}
	
	public FileException(String message) {
		super();
		this.message = message;
	}
	
	@Override
    public String getMessage(){
        return message;
    }
	

}
