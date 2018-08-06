package model;

import java.util.HashMap;

import exception.ArithmeticLogicUnitException;
import exception.BarrelExtensorException;
import exception.BarrelShifterException;
import exception.MultiplexerException;

public class ArithmeticLogicUnit {
	
	//Suggested signals
	public static final boolean SIGNAL_AND[] = {false, false, false};
	public static final boolean SIGNAL_OR[] = {false, false, true};
	public static final boolean SIGNAL_ADD[] = {false, true, false};
	public static final boolean SIGNAL_SUB[] = {true, true, false};
	
	//Operation
	public static final int OPERATION_AND = 1;
	public static final int OPERATION_OR = 2;
	public static final int OPERATION_ADD = 3;
	public static final int OPERATION_SUB = 4;
	
	private boolean zero;
	private boolean signal[];
	private int signalSize;
	private BitData input1;
	private BitData input2;
	private HashMap<BitData, Integer> signalMap;
	private String name;
	
	public ArithmeticLogicUnit(String name) throws MultiplexerException, ArithmeticLogicUnitException {
		super();
		
		this.signalSize = 3;
		this.name = name;
		
		boolean[] initSignal = new boolean[signalSize];
		for(int i = 0; i < signalSize; i++) {
			initSignal[i] = false;
		}
		
		this.signal = initSignal;
		this.zero = false;
		this.input1 = new BitData(32);
		this.input2 = new BitData(32);
		this.signalMap = new HashMap<BitData, Integer>();
		
		addInputSignal(SIGNAL_AND, OPERATION_AND);
		addInputSignal(SIGNAL_OR, OPERATION_OR);
		addInputSignal(SIGNAL_ADD, OPERATION_ADD);
		addInputSignal(SIGNAL_SUB, OPERATION_SUB);
	}
	
	public ArithmeticLogicUnit(String name, int signalSize) {
		super();
		
		this.signalSize = signalSize;
		this.name = name;
		
		boolean[] initSignal = new boolean[signalSize];
		for(int i = 0; i < signalSize; i++) {
			initSignal[i] = false;
		}
		
		this.signal = initSignal;
		this.zero = false;
		this.input1 = new BitData(32);
		this.input2 = new BitData(32);
		this.signalMap = new HashMap<BitData, Integer>();
	}
	
	public ArithmeticLogicUnit(String name, boolean[] initSignal) {
		super();
		
		this.name = name;
		this.signalSize = initSignal.length;
		this.signal = initSignal;
		this.zero = false;
		this.input1 = new BitData(32);
		this.input2 = new BitData(32);
		this.signalMap = new HashMap<BitData, Integer>();
	}
	
	public void addInputSignal(boolean[] signal, int operation) throws MultiplexerException, ArithmeticLogicUnitException {
		this.signalMap.put(BitData.booleanToBitData(signal), operation);
	}
	
	public void setSignal(boolean[] signal) throws ArithmeticLogicUnitException, MultiplexerException {
		if(signal.length != this.signalSize) {
			throw new ArithmeticLogicUnitException("Signal doesnt have the right size. Size need to be "+ this.signalSize);
		}
		else {
			this.signal = signal;
		}
	}
	
	public boolean[] getBooleanSignal() {
		return this.signal;
	}
	
	public BitData getBitDataSignal() {
		BitData signal = new BitData(this.signal.length);
		
		for(int i = 0; i < this.signal.length; i++) {
			signal.set(i, this.signal[i]);
		}
		
		return signal;
	}
	
	public BitData execute(BitData input1, BitData input2) throws MultiplexerException, ArithmeticLogicUnitException, BarrelShifterException, BarrelExtensorException {
		this.input1 = input1;
		this.input2 = input2;
		
		if(this.signalMap.get(getBitDataSignal()) == OPERATION_ADD) {
			return add(this.input1, this.input2); 
		}
		else if(this.signalMap.get(getBitDataSignal()) == OPERATION_SUB) {
			return sub(this.input1, this.input2);
		}
		else if(this.signalMap.get(getBitDataSignal()) == OPERATION_AND) {
			return and(this.input1, this.input2);
		}
		else if(this.signalMap.get(getBitDataSignal()) == OPERATION_OR) {
			return or(this.input1, this.input2);
		}
		else {
			throw new ArithmeticLogicUnitException("Invalid operation code. Code: " + this.signalMap.get(getBitDataSignal()));
		}
	}
	
	private BitData add(BitData a, BitData b) throws BarrelShifterException, BarrelExtensorException {
		BitData input1Aux = new BitData(a.length(), a);
		BitData input2Aux = new BitData(b.length(), b);
		
		BitData andAux = new BitData(input1Aux.length());
		BitData xorAux = new BitData(input2Aux.length());
		
		do {
			xorAux = new BitData(input1Aux.length(), input1Aux);
			xorAux.xor(input2Aux);
			
			andAux = new BitData(input1Aux.length(), input1Aux);
			andAux.and(input2Aux);
			
			input1Aux = xorAux;
			
			BarrelShifter shifter = new BarrelShifter("ALU " + this.name + " internal shifter");
			shifter.setInput(andAux, 1);
			input2Aux = shifter.getOutput();
			
		} while(!andAux.isEmpty());
		
		checkZero(input1Aux);
		
		return input1Aux;
	}
	
	private BitData sub(BitData a, BitData b) throws BarrelShifterException, BarrelExtensorException {
		BitData input1Aux = new BitData(a.length(), a);
		BitData input2Aux = new BitData(b.length(), b);
		
		input2Aux.flip(0, input2Aux.length());
		input1Aux = add(input1Aux, input2Aux);
		
		//Carry In = 1
		BitData carryin = new BitData(a.length());
		carryin.set(carryin.length()-1);
		
		//Add Carry
		
		input1Aux = add(input1Aux, carryin);
		
		checkZero(input1Aux);
		
		return input1Aux;
	}
	
	private BitData and(BitData a, BitData b) {
		BitData input1Aux = new BitData(a.length(), a);
		BitData input2Aux = new BitData(b.length(), b);
		
		input1Aux.and(input2Aux);
		
		checkZero(input1Aux);
		
		return input1Aux;
	}
	
	private BitData or(BitData a, BitData b) {
		BitData input1Aux = new BitData(a.length(), a);
		BitData input2Aux = new BitData(b.length(), b);
		
		input1Aux.or(input2Aux);
		
		checkZero(input1Aux);
		
		return input1Aux;
	}
	
	public boolean getZero() {
		return this.zero;
	}
	
	private void checkZero(BitData result) {
		if(result.isEmpty()) {
			this.zero = true;
		}
		else {
			this.zero = false;
		}
	}

	@Override
	public String toString() {
		String str = "Arithmetic Logic Unit - " + this.name + "\n";
		str += "Signal: " + BitData.booleanToBitData(this.signal) + " Size: " + this.signalSize + "\n";
		str += "Input 1: " + this.input1 + "\n"; 
		str += "Input 2: " + this.input2 + "\n";
		str += "Zero flag: " + this.zero + "\n";
		str += "Signal Map: \n";
		
		for(BitData key: this.signalMap.keySet()){
			String aux = this.signalMap.get(key) == 1 ? "And" : this.signalMap.get(key) == 2 ? "Or" : this.signalMap.get(key) == 3 ? "Add" : this.signalMap.get(key) == 4 ? "Sub" : "Error!";
			
			str += " - " + key + " = " + aux + "\n";
		}
		
		return str;
		
	}
	
	
	
	
}
