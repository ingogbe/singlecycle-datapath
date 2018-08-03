package model;

import exception.BarrelExtensorException;

public class BarrelExtensor {
	
	private BitData input;
	private int extendTo_nBits;
	
	public BarrelExtensor() {
		super();
		
		this.input = null;
		this.extendTo_nBits = -1;
	}
	
	public void setInput(BitData input, int extendTo_nBits) throws BarrelExtensorException {
		
		if(extendTo_nBits < input.length()) {
			throw new BarrelExtensorException("Can not extend to less than current value. Trying to extend " + input.length() + " to " + extendTo_nBits);
		}
		
		this.input = input;
		this.extendTo_nBits = extendTo_nBits;
	}
	
	public BitData getOutput() throws BarrelExtensorException {
		if(this.input != null) {
			BitData result = new BitData(extendTo_nBits, this.input);
			
			this.input = result;
			
			return this.input;
		}
		else {
			throw new BarrelExtensorException("Doesn't have input setted.");
		}
	}
}
