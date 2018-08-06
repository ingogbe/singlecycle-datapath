package model;
import java.util.BitSet;

import exception.BitDataException;

public class BitData extends BitSet{
	
	private static final long serialVersionUID = 1L;
	private int length;
	
	public BitData(int size) {
		super(size);
		
		this.length = size;
		this.set(0, size, false);
	}
	
	public BitData(int size, BitData bs) {
		super(size);
		
		this.length = size;
		this.set(0, size, false);
		
		for(int i = 0; i < size; i++) {
			if(bs.get(i)) {
				this.set(i);
			}
		}
		
	}
	
	public BitData(String bits) throws BitDataException {
		super(bits.length());
		
		this.length = bits.length();
		this.setBits(0, bits);
	}
	
	public BitData(boolean[] bits) {
		super(bits.length);
		
		this.length = bits.length;
		this.setBits(0, bits);
	}
	
	@Override
	public int length() {
		return length;
	}
	
	public boolean[] getBits(int from, int offset) {
		boolean bits[] = new boolean[offset];
		
		for(int i = from; i < (from + offset); i++) {
			bits[(i - from)] = this.get(i);
		}
		
		return bits;
	}
	
	public void setBits(int from, boolean[] bits) {
		for(int i = from; i < (from + bits.length); i++) {
			if(bits[i - from]) {
				this.set(i);
			}
		}
	}
	
	public void setBits(int from, String bits) throws BitDataException {
		if(bits.matches("^(0|1)*$")) {
			for(int i = from; i < (from + bits.length()); i++) {
				if(bits.charAt(i - from) == '1') {
					this.set(i);
				}
			}
		}
		else {
			throw new BitDataException("Invalid bit string. Not contain only 0's and 1's");
		}
		
	}
	
	public BitData getInverted() {
		BitData new_bd = new BitData(this.length);
		
		for(int i = (this.length -  1); i >=0; i--) {
			new_bd.set((this.length - 1) - i, this.get(i));
		}
		
		return new_bd;
	}
	
	// Get char equivalent of bit on 'index'
	public char getCharBit(int index) {
		if(this.get(index)) {
			return '1';
		}
		else {
			return '0';
		}
	}

	@Override
	public String toString() {
		String ins = "";
		
		for(int i = 0; i < this.length; i++) {
			if(i == (this.length - 1)) {
				ins = ins + this.getCharBit(i);
			}
			else {
				ins = ins + this.getCharBit(i) + " ";
			}
		}
		
		return ins;
	}
	
	public String toString(String bitSufix) {
		String ins = "";
		
		for(int i = 0; i < this.length; i++) {
			if(i == (this.length - 1)) {
				ins = ins + this.getCharBit(i);
			}
			else {
				ins = ins + this.getCharBit(i) + bitSufix;
			}
		}
		
		return ins;
	}
	
	
	public String toStringInvert() {
		String ins = "";
		
		for(int i = this.length - 1; i >= 0; i--) {
			ins = ins + " " + this.getCharBit(i);
		}
		
		return ins;
	}
	
	public long toDecimal() {
		long decimal = 0;
		
		for(int i = this.length() - 1; i >= 0; i--) {
			if(get(i)) {
				decimal = (long) (decimal + Math.pow(2, (this.length() - 1) - i));
			}
		}
		
		return decimal;
	}
	
	public String toZeroPadDecimal() {
		long decimal = 0;
		long max_number = 0;
		
		for(int i = this.length() - 1; i >= 0; i--) {
			if(get(i)) {
				decimal = (long) (decimal + Math.pow(2, (this.length() - 1) - i));
			}
			
			max_number = (long) (max_number + Math.pow(2, (this.length() - 1) - i));
		}
		
		return String.format("%0" + String.valueOf(max_number).length() + "d", decimal);
	}
	
	
	
	public static BitData booleanToBitData(boolean[] b) {
		BitData bd = new BitData(b.length);
		
		for(int i = 0; i < b.length; i++) {
			bd.set(i, b[i]);
		}
		
		return bd;
	}
	
	public static boolean[] bitDataToBoolean(BitData bd) {
		boolean b[] = new boolean[bd.length];
		
		for(int i = 0; i < b.length; i++) {
			b[i] = bd.get(i);
		}
		
		return b;
	}
	
	
	
}
