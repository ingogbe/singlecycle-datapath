package model;


import java.io.IOException;
import java.util.HashMap;

import javax.swing.UnsupportedLookAndFeelException;

import controller.FileController;
import exception.ArithmeticLogicUnitException;
import exception.BarrelExtensorException;
import exception.BarrelShifterException;
import exception.BitDataException;
import exception.FileException;
import exception.InstructionException;
import exception.InstructionMemoryException;
import exception.MultiplexerException;

public class InstructionMemory {
	private HashMap<BitData, Instruction> memory;
	private String name;
	
	public InstructionMemory(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, FileException, IOException, InstructionMemoryException, InstructionException, BitDataException, ArithmeticLogicUnitException, MultiplexerException, BarrelShifterException, BarrelExtensorException{
		super();
		
		this.memory = new HashMap<BitData, Instruction>();
		this.name = name;
		
		loadInstructions();
	}
	
	private void loadInstructions() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, FileException, IOException, InstructionMemoryException, InstructionException, BitDataException, ArithmeticLogicUnitException, MultiplexerException, BarrelShifterException, BarrelExtensorException{
		FileController fc = new FileController(FileController.CHOOSER_TYPE_LOAD, false);
		fc.chooseFile();
		
		String instr = null;
		BitData address = new BitData("00000000000000000000000000000000");
		BitData four = new BitData("00000000000000000000000000000100");
		
		do {
			instr = fc.readLine();
			
			if(instr != null) {
				instr = instr.trim();
				
				if(!instr.isEmpty()) {
					addInstruction(address, new Instruction(instr));

					ArithmeticLogicUnit alu = new ArithmeticLogicUnit("Data memory " + this.name + " internal ULA", ArithmeticLogicUnit.SIGNAL_ADD);
					alu.addInputSignal(ArithmeticLogicUnit.SIGNAL_ADD, ArithmeticLogicUnit.OPERATION_ADD);
					address = alu.execute(address, four);
					
				}
			}
			
		} while(instr != null);
	}
	
	public void addInstruction(BitData address, Instruction instruction) throws InstructionMemoryException {
		if(this.memory.get(address) != null) {
			throw new InstructionMemoryException("This address is already taken. Address " + address);
		}
		else {
			this.memory.put(address, instruction);
		}
	}
	
	public Instruction getInstruction(BitData address) throws InstructionMemoryException {
		if(address.length() == Instruction.INSTRUCTION_SIZE) {
			if(this.memory.get(address) != null) {
				return this.memory.get(address);
			}
			else {
				throw new InstructionMemoryException("This address is empty. Address " + address);
			}
		}
		else {
			throw new InstructionMemoryException("Wrong address size. Address has " + address.length() + " bit(s), but need " + Instruction.INSTRUCTION_SIZE + " bit(s). Address = " + address);
		}
		
	}

	@Override
	public String toString() {
		String all_instr = "Instruction memory - " + this.name + "\n";
		
		for (BitData key: this.memory.keySet()){
			Instruction instr = this.memory.get(key);
			
			BitData value = BitData.booleanToBitData(instr.getBits(0, Instruction.INSTRUCTION_SIZE));
			all_instr = all_instr + "Instruction: " + value.toString("") + " | " + value.length() + " | Address: " + key.toString("") + " (" + key.toDecimal() + ")\n";
		}
		
		return all_instr;
	}
	
	
}
