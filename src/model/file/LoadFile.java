package model.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoadFile {
	
	private File file;
	private BufferedReader buffer;
	
	//Constructors
	public LoadFile(File file) throws FileNotFoundException{
		super();
		
		this.file = file;
		
		loadBuffer();
	}
	
	//Getters and Setters
	public File getFile() {
		return file;
	}

	public BufferedReader getBuffer() {
		return buffer;
	}
	
	//Other private functions
	private void loadBuffer() throws FileNotFoundException {
		this.buffer = new BufferedReader(new FileReader(this.file));
	}
	
	private void resetBuffer() throws IOException {
		closeBuffer();
		loadBuffer();
	}
	
	
	//Other public functions
	public String readLine() throws IOException{
		String content = getBuffer().readLine();
		return content;
	}
	
	public void skipLine(int numberOfLines) throws IOException{
		for(int i = 0; i < numberOfLines; i++)
			getBuffer().readLine();
	}
	
	public void closeBuffer() throws IOException{
		getBuffer().close();
	}
	
	public int countLines() throws IOException {
		int cnt = 0;
		while(readLine() != null) {
			cnt++;
		}
		resetBuffer();
		return cnt;		
	}
	
}