package model;

import java.util.BitSet;

import exception.BarrelExtensorException;
import exception.BarrelShifterException;

public class BarrelShifter {
	
	private BitData input;
	private int n_bits_shift;
	
	public BarrelShifter() {
		super();
		
		this.input = null;
		this.n_bits_shift = -1;
	}
	
	public void setInput(BitData input, int extendTo_nBits) throws BarrelExtensorException {		
		this.input = input;
		this.n_bits_shift = extendTo_nBits;
	}
	
	public BitData getOutput() throws BarrelShifterException {
		
		if(this.input != null) {
			BitSet result = this.input.get(n_bits_shift, Math.max(n_bits_shift, this.input.length()));
			
			for(int i = 0; i < this.input.length(); i++) {
				if(result.get(i)) {
					this.input.set(i);
				}
				else {
					this.input.clear(i);
				}
			}
			
			return this.input;
		}
		else {
			throw new BarrelShifterException("Doesn't have input setted.");
		}
		
	}
}
