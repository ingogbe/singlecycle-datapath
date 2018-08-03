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
	
	public DataMemory(boolean initWriteSignal, boolean initReadSignal) {
		super();
		
		this.writeSignal = initWriteSignal;
		this.readSignal = initReadSignal;
	}
	
	public DataMemory() throws BitDataException, MultiplexerException, ArithmeticLogicUnitException, BarrelShifterException, BarrelExtensorException {
		super();
		ArithmeticLogicUnit alu = new ArithmeticLogicUnit(ArithmeticLogicUnit.SIGNAL_ADD);
		alu.addInputSignal(ArithmeticLogicUnit.SIGNAL_ADD, ArithmeticLogicUnit.OPERATION_ADD);
		
		this.writeSignal = false;
		this.readSignal = false;
		this.length = (int) Math.pow(2, ADDRESS_SIZE);
		this.memory = new HashMap<BitData, BitData>();
		
		this.address = new BitData(ADDRESS_SIZE);
		
		System.out.println(length);
		
		BitData address = new BitData(32);
		BitData one = new BitData("00000000000000000000000000000001");
		
		for(int i = 0; i < this.length; i++) {
			memory.put(address, new BitData(DATA_SIZE));
			address = alu.execute(address, one);
		}
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
		else {
			throw new DataMemoryException("You can't read data from memory without set 'read' signal.");
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
					this.memory.put(this.address, data);
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

	@Override
	public String toString() {
		String regs = "Length: " + length + "\n";
		regs = regs + "Write Signal: " + (writeSignal ? 1 : 0) + "\n";
		regs = regs + "Read Signal: " + (readSignal ? 1 : 0) + "\n";
		
		regs = regs + "Current address:\n Address[" + address.toString("") + "](" + address.toZeroPadDecimal() + ") Value[" + this.memory.get(address).toString("") + "]\n";
		
		regs = regs + "All memory addresses:\n";
		for (BitData key: this.memory.keySet()){
			regs = regs + " - Address[" + key.toString("") + "](" + key.toZeroPadDecimal() + ") Value[" + this.memory.get(key).toString("") + "]\n";
		}
		
		return regs;
	}
}
