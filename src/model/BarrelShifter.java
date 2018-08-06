package model;

import java.util.BitSet;

import exception.BarrelExtensorException;
import exception.BarrelShifterException;

public class BarrelShifter {
	
	private BitData input;
	private int n_bits_shift;
	private String name;
	
	public BarrelShifter(String name) {
		super();
		
		this.input = null;
		this.n_bits_shift = -1;
		this.name = name;
	}
	
	public void setInput(BitData input, int extendTo_nBits) throws BarrelExtensorException {		
		this.input = input;
		this.n_bits_shift = extendTo_nBits;
	}
	
	public BitData getOutput() throws BarrelShifterException {			
		if(this.input != null) {
			BitData aux = new BitData(this.input.length());
			
			BitSet result = this.input.get(n_bits_shift, Math.max(n_bits_shift, this.input.length()));
			
			for(int i = 0; i < this.input.length(); i++) {
				if(result.get(i)) {
					aux.set(i);
				}
				else {
					aux.clear(i);
				}
			}
			
			return aux;
		}
		else {
			throw new BarrelShifterException("Doesn't have input setted.");
		}
		
	}

	@Override
	public String toString() {
		String str = "Barrel Shifter - " + this.name + "\n";
		str += "Input: " + this.input + " / Size: " + this.input.length() + "\n";
		str += "Shift " + this.n_bits_shift + " bit(s)\n";
		try {
			str += "Output: " + this.getOutput() + " / Size: " + this.getOutput().length();
		} catch (BarrelShifterException e) {
			str += "Output: no input setted";
		}
		
		return str;
	}
	
	
}
