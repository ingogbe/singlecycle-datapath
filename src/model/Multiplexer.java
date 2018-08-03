package model;
import java.util.HashMap;
import java.util.Map;

import exception.MultiplexerException;

public class Multiplexer {
	
	private Map<BitData, BitData> inputs;
	
	private boolean[] controlSignal;
	private int signalSize;
	
	public Multiplexer(boolean[] initSignal) {
		super();
		
		this.signalSize = initSignal.length;
		this.controlSignal = initSignal;
		
		this.inputs = new HashMap<BitData, BitData>();
	}
	
	public Multiplexer(int signalSize) {
		super();
		
		this.signalSize = signalSize;
		
		boolean[] initSignal = new boolean[signalSize];
		for(int i = 0; i < signalSize; i++) {
			initSignal[i] = false;
		}
		
		this.controlSignal = initSignal;
		
		this.inputs = new HashMap<BitData, BitData>();
	}
	
	public void setSignal(boolean[] signal) throws MultiplexerException {
		if(signal.length != this.signalSize) {
			throw new MultiplexerException("Signal doesnt have the right size. Size need to be "+ this.signalSize);
		}
		else {
			this.controlSignal = signal;
		}
	}
	
	// Overload current signal if signal already have value;
	public void addInput(BitData signal, BitData output) throws MultiplexerException {
		
		if(signal.length() == this.signalSize) {
			this.inputs.put(signal, output);
		}
		else {
			throw new MultiplexerException("Invalid signal! Signal size should be " + this.signalSize + " bit(s). Current signal: [" + signal + "]. Current signal size: " + signal.length() + " bit(s)");
		}
	}
	
	public void resetInputs() {
		this.inputs.clear();
	}
	
	public BitData getOutput() throws MultiplexerException {
		BitData output = this.inputs.get(getBitDataSignal());
		
		if(output == null) {
			throw new MultiplexerException("The current signal doesn't has an input. Signal: " + getBitDataSignal());
		}
		else {
			return output;
		}
	}
	
	public Map<BitData, BitData> getInputs() {
		return this.inputs;
	}
	
	public BitData getBitDataSignal() {
		BitData signal = new BitData(this.controlSignal.length);
		
		for(int i = 0; i < this.controlSignal.length; i++) {
			signal.set(i, this.controlSignal[i]);
		}
		
		return signal;
	}
	
	public boolean[] getBooleanSignal() {		
		return this.controlSignal;
	}

	
	@Override
	public String toString() {
		
		String current_signal = "Signal size: " + signalSize + "\nCurrent signal: " + BitData.booleanToBitData(this.controlSignal) + "\n";
		String str_inputs = "Inputs:\n";
		String str_output = "Output: ";
		
		for (BitData key: this.inputs.keySet()){
			str_inputs = str_inputs + " - Signal[" + key.toString("") + "](" + key.toZeroPadDecimal() + ") Value[" + this.inputs.get(key).toString("") + "]\n";  
		}
		
		BitData output = this.inputs.get(getBitDataSignal());
		
		if(output == null) {
			str_output = str_output + "No output";
		}
		else {
			str_output = str_output + output.toString("");
		}
		
		return current_signal + str_inputs + str_output;
	}
	
	
	
	
	
	
	
	
	
}
