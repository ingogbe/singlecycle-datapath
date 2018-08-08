package model;
import exception.BitDataException;
import exception.InstructionException;

public class Instruction {
	// Instruction sizes
	public static final int INSTRUCTION_SIZE = 32;
	public static final int OPCODE_SIZE = 6;
	public static final int RS_SIZE = 5;
	public static final int RT_SIZE = 5;
	public static final int RD_SIZE = 5;
	public static final int SHAMT_SIZE = 5;
	public static final int FUNCT_SIZE = 6;
	public static final int CONST_SIZE = 16;
	public static final int ADDR_SIZE = 26;
	
	// Instruction types
	public static final int FORMAT_R = 0;
	public static final int FORMAT_I = 1;
	public static final int FORMAT_J = 2;
	public static final int FORMAT_NOT_RECOGNIZED = 3;
	
	private BitData instruction;
	private int type;

	// Constructor, create a new 0 instruction
	public Instruction() throws InstructionException {
		super();
		
		this.type = FORMAT_NOT_RECOGNIZED;
		this.instruction = new BitData(INSTRUCTION_SIZE);
	}
	
	// Constructor, create a new 0 instruction
	public Instruction(boolean[] instruction) throws InstructionException {
		super();
		
		this.type = FORMAT_NOT_RECOGNIZED;
		this.instruction = new BitData(INSTRUCTION_SIZE);
		
		this.setInstruction(instruction);
	}
	
	// Constructor, create a new instruction
	public Instruction(String instruction) throws InstructionException, BitDataException {
		super();
		
		this.type = FORMAT_NOT_RECOGNIZED;
		this.instruction = new BitData(INSTRUCTION_SIZE);
		
		this.setInstruction(instruction.trim());
	}
	
	// Set full instruction with values from a boolean array
	// If parameter doesn't have the required size, throws exception
	public void setInstruction(boolean[] instruction) throws InstructionException {
		if(instruction.length == INSTRUCTION_SIZE) {
			this.instruction.setBits(0, instruction);
		}
		else {
			throw new InstructionException("Invalid instruction. " + instruction.length + " bit(s) found, but need " + INSTRUCTION_SIZE + " bit(s)");
		}
	}
	
	// Set full instruction with values from a String
	// If parameter doesn't have the required size, throws exception
	public void setInstruction(String instruction) throws InstructionException, BitDataException {
		if(instruction.length() == INSTRUCTION_SIZE) {
			this.instruction.setBits(0, instruction);
		}
		else {
			throw new InstructionException("Invalid instruction. " + instruction.length() + " bit(s) found, but need " + INSTRUCTION_SIZE + " bit(s)");
		}
	}
	
	// Get 'opcode' from Data (bit array)
	public boolean[] getOpcode() {	
		return this.instruction.getBits(0, OPCODE_SIZE);
	}
	
	// Get 'RS' from Data (bit array)
	public boolean[] getRS() {
		return this.instruction.getBits(OPCODE_SIZE, RS_SIZE);
	}
	
	// Get 'RT' from Data (bit array)
	public boolean[] getRT() {
		return this.instruction.getBits(OPCODE_SIZE + RS_SIZE, RT_SIZE);
	}
	
	// Get 'RD' from Data (bit array)
	public boolean[] getRD() {
		return this.instruction.getBits(OPCODE_SIZE + RS_SIZE + RT_SIZE, RD_SIZE);
	}

	// Get 'shamt' from Data (bit array)
	public boolean[] getShamt() {
		return this.instruction.getBits(OPCODE_SIZE + RS_SIZE + RT_SIZE + RD_SIZE, SHAMT_SIZE);
	}
	
	// Get 'funct' from Data (bit array)
	public boolean[] getFunct() {
		
		boolean[] aaaa = this.instruction.getBits(OPCODE_SIZE + RS_SIZE + RT_SIZE + RD_SIZE + SHAMT_SIZE, FUNCT_SIZE);
		
		return aaaa;
	}
	
	// Get 'const' from Data (bit array)
	public boolean[] getConst() {
		return this.instruction.getBits(OPCODE_SIZE + RS_SIZE + RT_SIZE, CONST_SIZE);
	}
	
	// Get 'addr' from Data (bit array)
	public boolean[] getAddr() {
		return this.instruction.getBits(OPCODE_SIZE, ADDR_SIZE);
	}
	
	// Get 'n bits' from Data (bit array)
		public boolean[] getBits(int from, int offset) {
			return this.instruction.getBits(from, offset);
		}
	
	// Get 'type' from instruction
	public int getType() {
		return this.type;
	}
	
	// Set 'type' for instruction
	public void setType(int type) throws InstructionException {
		if(type < 0 || type > 2) {
			throw new InstructionException("Invalid instruction format.");
		}
		else {
			this.type = type;
		}
	}
	
	// Get 'type' name from instruction
	public String getTypeName() {
		if(this.type == FORMAT_R) {
			return "R format";
		}
		else if(this.type == FORMAT_I) {
			return "I format";
		}
		else if(this.type == FORMAT_J) {
			return "J format";
		}
		else {
			return "Instruction format not recognized yet";
		}
	}
	
	// Print (pretty) instruction data
	@Override
	public String toString() {
		String ins = "";
		String pos = "";
		
		String opcode = " OPCODE          ";
		String rs = " RS           ";
		String rt = " RT           ";
		String rd = " RD           ";
		String shamt = " SHAMT        ";
		String funct = " FUNCT           ";
		String const_end = " CONST/END                                     ";
		String end = " END                                                                         ";
		
		String r_instruction = "[" + opcode + "|" + rs + "|" + rt + "|" + rd + "|" + shamt + "|" + funct + "] <= R format";
		String i_instruction = "[" + opcode + "|" + rs + "|" + rt + "|" + const_end + "] <= I format";
		String j_instruction = "[" + opcode + "|" + end + "] <= J format";
		String x_instruction = "[" + opcode + "|" + "                                                                             ] <= Format not recognized yet";
		
		for(int i = 0; i < INSTRUCTION_SIZE; i++) {
			if(i < 22) {
				pos += (INSTRUCTION_SIZE - 1 - i) + "|";
				ins = ins + " " + this.instruction.getCharBit(i) + "|";
			}
			else {
				if(i == (INSTRUCTION_SIZE - 1)) {
					ins = ins + " " + this.instruction.getCharBit(i);
					pos += " " + (INSTRUCTION_SIZE - 1 - i);
				}
				else {
					ins = ins + " " + this.instruction.getCharBit(i) + "|";
					pos += " " + (INSTRUCTION_SIZE - 1 - i) + "|";
				}
			}
		}
		
		ins = "[" + ins + "] <= Instruction";
		pos = "[" + pos + "] <= Position";
		
		String str = ins + "\n" + pos;
		
		if(this.type == FORMAT_R) {
			str = str + "\n" + r_instruction+ "\n";
		}
		else if(this.type == FORMAT_I) {
			str = str + "\n" + i_instruction+ "\n";
		}
		else if(this.type == FORMAT_J) {
			str = str + "\n" + j_instruction+ "\n";
		}
		else {
			str = str + "\n" + x_instruction+ "\n";
		}
		
		return str;
	}
	
	
	
	
}
