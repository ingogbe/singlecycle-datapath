package model;

import exception.BarrelExtensorException;

public class BarrelExtensor {
	
	private BitData input;
	private int extendTo_nBits;
	private String name;
	
	public BarrelExtensor(String name) {
		super();
		
		this.input = null;
		this.extendTo_nBits = -1;
		this.name = name;
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
			BitData result = new BitData(32);
			
			for(int i = this.input.length(); i < this.extendTo_nBits; i++) {
				if(this.input.get(i - (this.input.length()))) {
					result.set(i);
				}
			}
			
			return result;
		}
		else {
			throw new BarrelExtensorException("Doesn't have input setted.");
		}
	}
	
	@Override
	public String toString() {
		String str = "Barrel Extensor - " + this.name + "\n";
		str += "Input: " + this.input + "\n";
		str += "Extended " + this.extendTo_nBits + " bit(s)\n";
		try {
			str += "Output: " + this.getOutput();
		} catch (BarrelExtensorException e) {
			str += "Output: no input setted";
		}
		
		return str;
	}
}
