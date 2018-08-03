package model.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFile {
	
	private File file;
	private BufferedWriter buffer;
	private FileWriter writer;
	private boolean append;
	
	//Constructors
	public SaveFile(File file, boolean append) throws IOException{
		super();
		this.file = file;
		this.append = append;
		loadBuffer();
	}

	//Getters and Setters
	public File getFile() {
		return this.file;
	}

	public BufferedWriter getBuffer() {
		return this.buffer;
	}
	
	public FileWriter getWriter() {
		return this.writer;
	}
	
	public boolean isAppend() {
		return this.append;
	}
	
	//Other private functions
	private void loadBuffer() throws IOException {
		
		String filename = this.file.getAbsolutePath();
		
		if(!filename.toLowerCase().endsWith(".txt")){
			filename = filename + ".txt";
		}
		
		this.writer = new FileWriter(filename, isAppend());
		this.buffer = new BufferedWriter(this.writer);
	}


	//Other public functions
	public void writeFile(String content) throws IOException {
		getBuffer().write(content + "\n");
	}
	
	public void closeBuffer() throws IOException{
		getBuffer().close();
		getWriter().close();
	}
	
}