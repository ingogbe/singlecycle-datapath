package model;

import java.util.HashMap;

import exception.ArithmeticLogicUnitException;
import exception.BarrelExtensorException;
import exception.BarrelShifterException;
import exception.BitDataException;
import exception.MultiplexerException;
import exception.RegistersException;

public class Registers {
	
	public static final int REGISTER_SIZE = 32;
	public static final int READ_ADDRESS_SIZE = 5;
	public static final int WRITE_ADDRESS_SIZE = 5;
	
	private int length;
	private HashMap<BitData, BitData> registers;
	
	private BitData readRegisterAddress1;
	private BitData readRegisterAddress2;
	private BitData writeRegisterAddress1;
	
	private boolean writeSignal;
	
	public Registers(boolean initSignal) {
		super();
		
		this.writeSignal = initSignal;
	}
	
	public Registers() throws BitDataException, MultiplexerException, ArithmeticLogicUnitException, BarrelShifterException, BarrelExtensorException {
		super();
		ArithmeticLogicUnit alu = new ArithmeticLogicUnit(ArithmeticLogicUnit.SIGNAL_ADD);
		alu.addInputSignal(ArithmeticLogicUnit.SIGNAL_ADD, ArithmeticLogicUnit.OPERATION_ADD);
		
		this.writeSignal = false;
		this.length = 32;
		this.registers = new HashMap<BitData, BitData>();
		
		this.readRegisterAddress1 = new BitData(READ_ADDRESS_SIZE);
		this.readRegisterAddress2 = new BitData(READ_ADDRESS_SIZE);
		this.writeRegisterAddress1 = new BitData(WRITE_ADDRESS_SIZE);
		
		BitData address = new BitData(5);
		BitData one = new BitData("00001");
		
		for(int i = 0; i < this.length; i++) {
			registers.put(address, new BitData(REGISTER_SIZE));
			address = alu.execute(address, one);
		}
	}
	
	public BitData getReadData1() throws RegistersException {
		BitData regData = this.registers.get(this.readRegisterAddress1);
		
		if(regData != null) {
			return this.registers.get(this.readRegisterAddress1);
		}
		else {
			throw new RegistersException("Data from register 1 address [" + this.readRegisterAddress1.toString("") + "] is null");
		}
	}
	
	public BitData getReadData2() throws RegistersException {
		BitData regData = this.registers.get(this.readRegisterAddress2);
		
		if(regData != null) {
			return this.registers.get(this.readRegisterAddress2);
		}
		else {
			throw new RegistersException("Data from register 2 address [" + this.readRegisterAddress2.toString("") + "] is null");
		}
	}
	
	public void setWriteSignal(boolean signal) {
		this.writeSignal = signal;
	}
	
	public void writeData(BitData data) throws RegistersException {
		if(this.writeSignal) {
			if(data.length() == REGISTER_SIZE) {
				if(this.writeRegisterAddress1 != null) {
					this.registers.put(this.writeRegisterAddress1, data);
				}
				else{
					throw new RegistersException("Invalid write address! Address: " + this.writeRegisterAddress1);
				}
				
			}
			else {
				throw new RegistersException("Register data need to have " + REGISTER_SIZE + " bit(s), but has " + data.length() + " bit(s)");
			}
		}
	}
	
	public void setWriteRegister1(BitData address) throws RegistersException {
		if(address.length() == WRITE_ADDRESS_SIZE) {
			this.writeRegisterAddress1 = address;
		}
		else {
			throw new RegistersException("Address need to have " + WRITE_ADDRESS_SIZE + " bit(s), but has " + address.length() + " bit(s)");
		}
	}
	
	public void setReadRegister1(BitData address) throws RegistersException {
		if(address.length() == READ_ADDRESS_SIZE) {
			this.readRegisterAddress1 = address;
		}
		else {
			throw new RegistersException("Address need to have " + READ_ADDRESS_SIZE + " bit(s), but has " + address.length() + " bit(s)");
		}
	}
	
	public void setReadRegister2(BitData address) throws RegistersException {
		if(address.length() == READ_ADDRESS_SIZE) {
			this.readRegisterAddress2 = address;
		}
		else {
			throw new RegistersException("Address need to have " + READ_ADDRESS_SIZE + " bit(s), but has " + address.length() + " bit(s)");
		}
	}

	@Override
	public String toString() {
		String regs = "Length: " + length + "\n";
		regs = regs + "Write Signal: " + (writeSignal ? 1 : 0) + "\n";
		
		regs = regs + "Read register 1: Address[" + readRegisterAddress1.toString("") + "](" + readRegisterAddress1.toZeroPadDecimal() + ") Value[" + this.registers.get(readRegisterAddress1).toString("") + "]\n";
		regs = regs + "Read register 2: Address[" + readRegisterAddress2.toString("") + "](" + readRegisterAddress2.toZeroPadDecimal() + ") Value[" + this.registers.get(readRegisterAddress2).toString("") + "]\n";
		regs = regs + "Write register : Address[" + writeRegisterAddress1.toString("") + "](" + writeRegisterAddress1.toZeroPadDecimal() + ") Value[" + this.registers.get(writeRegisterAddress1).toString("") + "]\n";
		
		regs = regs + "All registers:\n";
		for (BitData key: this.registers.keySet()){
			regs = regs + " - Address[" + key.toString("") + "](" + key.toZeroPadDecimal() + ") Value[" + this.registers.get(key).toString("") + "]\n";
		}
		
		return regs;
	}
}
