package model;

import java.util.HashMap;

import exception.ArithmeticLogicUnitException;
import exception.BarrelExtensorException;
import exception.BarrelShifterException;
import exception.BitDataException;
import exception.DataMemoryException;
import exception.MultiplexerException;

public class DataMemory {
	
	public static final int DATA_SIZE = 32;
	public static final int ADDRESS_SIZE = 32;
	
	private int length;
	private HashMap<BitData, BitData> memory;
	
	private BitData address;
	
	private boolean writeSignal;
	private boolean readSignal;
	private String name;
	
	public DataMemory(String name, boolean initWriteSignal, boolean initReadSignal) {
		super();
		
		this.writeSignal = initWriteSignal;
		this.readSignal = initReadSignal;
		this.name = name;
	}
	
	public DataMemory(String name) throws BitDataException, MultiplexerException, ArithmeticLogicUnitException, BarrelShifterException, BarrelExtensorException {
		super();
		
		this.writeSignal = false;
		this.readSignal = false;
		this.length = 0;
		this.memory = new HashMap<BitData, BitData>();
		this.name = name;
		
		this.address = new BitData(ADDRESS_SIZE);
	}
	
	public BitData readData() throws DataMemoryException {
		if(this.readSignal) {
			BitData regData = this.memory.get(this.address);
			
			if(regData != null) {
				return this.memory.get(this.address);
			}
			else {
				throw new DataMemoryException("Data from memory address [" + this.address.toString("") + "] is null");
			}
		}
		else{
			return null;
		}
	}
	
	public void setWriteSignal(boolean signal) {
		this.writeSignal = signal;
	}
	
	public void setReadSignal(boolean signal) {
		this.readSignal = signal;
	}
	
	public void writeData(BitData data) throws DataMemoryException {
		if(this.writeSignal) {
			if(data.length() == DATA_SIZE) {
				if(this.address != null) {
					BitData value = this.memory.get(this.address);
					
					if(value == null){
						this.memory.put(this.address, data);
						this.length++;
					}
					else{
						//Override current memory value
						this.memory.put(this.address, data);
					}
					
				}
				else{
					throw new DataMemoryException("Invalid write address! Address: " + this.address);
				}
			}
			else {
				throw new DataMemoryException("Register data need to have " + DATA_SIZE + " bit(s), but has " + data.length() + " bit(s)");
			}
		}
	}
	
	public void setAddress(BitData address) throws DataMemoryException {
		if(address.length() == ADDRESS_SIZE) {
			this.address = address;
		}
		else {
			throw new DataMemoryException("Address need to have " + ADDRESS_SIZE + " bit(s), but has " + address.length() + " bit(s)");
		}
	}
	
	public BitData getAddress(){
		return this.address;
	}

	@Override
	public String toString() {
		String regs = "Data memory - " + this.name + "\n";
		regs += "Length: " + length + "\n";
		regs = regs + "Write Signal: " + (writeSignal ? 1 : 0) + "\n";
		regs = regs + "Read Signal: " + (readSignal ? 1 : 0) + "\n";
		
		String currentAddressValue = (this.memory.get(this.address) != null) ? this.memory.get(this.address).toString("") : "empty";
		
		regs = regs + "Current address:\n Address[" + this.address.toString("") + "](" + this.address.toZeroPadDecimal() + ") Value[" + currentAddressValue + "]\n";
		
		regs = regs + "All memory addresses:\n";
		if(this.length == 0){
			regs = regs + " - empty";
		}
		else{
			for (BitData key: this.memory.keySet()){
				regs = regs + " - Address[" + key.toString("") + "](" + key.toZeroPadDecimal() + ") Value[" + this.memory.get(key).toString("") + "]\n";
			}
		}	
		
		return regs;
	}
}
