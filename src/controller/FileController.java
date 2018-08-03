package controller;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.file.LoadFile;
import model.file.SaveFile;
import exception.FileException;

public class FileController {
	
	private File file;
	private LoadFile colLoadFile;
	private SaveFile colSaveFile;
	private int chooseType;
	private boolean append;
	
	public static final int CHOOSER_TYPE_LOAD = 1;
	public static final int CHOOSER_TYPE_SAVE = 2;
	
	//Constructors
	public FileController(int chooseType, boolean append){
		this.file = null;
		this.colLoadFile = null;
		this.colSaveFile = null;
		this.chooseType = chooseType;
		this.append = append;
	}
	
	//Getters and Setters
	public File getFile() {
		return file;
	}
	
	public LoadFile getColLoadFile() {
		return colLoadFile;
	}
	
	public SaveFile getColSaveFile() {
		return colSaveFile;
	}
	
	public int getChooseType() {
		return this.chooseType;
	}
	
	public boolean isAppend() {
		return this.append;
	}
	
	
	//Other functions
	public boolean fileIsEmpty() {
		if(getFile() == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public File chooseFile() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, FileException, IOException{
		File file = null;
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());	
		JFileChooser fileChooser = new JFileChooser();
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
		fileChooser.setFileFilter(filter);
		
		if(getChooseType() == CHOOSER_TYPE_LOAD) {
			fileChooser.setDialogTitle("Selecione o arquivo de entrada do algoritmo");
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				this.file = file;
				this.colLoadFile = new LoadFile(file);
				return file;
			}
			else {
				throw new FileException("No file selected.");
			}
		}
		else if(getChooseType() == CHOOSER_TYPE_SAVE) {
			fileChooser.setDialogTitle("Selecione o arquivo para salvar a sa�da do algoritmo");
			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				this.file = file;
				this.colSaveFile = new SaveFile(file, isAppend());
				return file;
			}
			else {
				throw new FileException("No file selected.");
			}
		}
		else {
			throw new FileException("Invalid chooser type.");
		}
		
	}
	
	public int countLines() throws IOException, FileException {
		if(!fileIsEmpty()) {
			if(getColLoadFile() != null)
				return getColLoadFile().countLines();
			else
				throw new FileException("The file choosed must be load type.");
		}
		else {
			throw new FileException("There is no file selected.");
		}
	}
	
	public String readLine() throws IOException, FileException {
		if(!fileIsEmpty()) {
			if(getColLoadFile() != null)
				return getColLoadFile().readLine();
			else
				throw new FileException("The file choosed must be load type.");
		}
		else {
			throw new FileException("There is no file selected.");
		}
	}
	
	public void skipLines(int numberOfLines) throws IOException, FileException {
		if(!fileIsEmpty()) {
			if(getColLoadFile() != null)
				getColLoadFile().skipLine(numberOfLines);
			else
				throw new FileException("The file choosed must be load type.");
		}
		else {
			throw new FileException("There is no file selected.");
		}
	}
	
	public void writeFile(String content) throws FileException, IOException {
		if(!fileIsEmpty()) {
			if(getColSaveFile() != null)
				getColSaveFile().writeFile(content);
			else
				throw new FileException("The file choosed must be save type.");
		}
		else {
			throw new FileException("There is no file selected.");
		}
	}
	
	public void closeBuffer() throws IOException {
		if(getColLoadFile() != null)
			getColLoadFile().closeBuffer();
		if(getColSaveFile() != null)
			getColSaveFile().closeBuffer();
	}

	

	
}