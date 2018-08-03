package model;
import java.util.HashMap;
import java.util.Map;

import exception.ControlUnitException;

public class ControlUnit {
	
	// input
	private BitData current_opcode;
	
	// outputs helpers
	public static final String OUTPUT_LI = "li";
	public static final String OUTPUT_JUMP = "jump";
	public static final String OUTPUT_REG_DST = "regDst";
	public static final String OUTPUT_ALU_SRC = "aluSrc";
	public static final String OUTPUT_MEM_TO_REG = "memToReg";
	public static final String OUTPUT_REG_WRITE = "regWrite";
	public static final String OUTPUT_MEM_READ = "memRead";
	public static final String OUTPUT_MEM_WRITE = "memWrite";
	public static final String OUTPUT_BRANCH = "branch";
	public static final String OUTPUT_ALU_OP_1 = "aluOP1";
	public static final String OUTPUT_ALU_OP_0 = "aluOP0";
	public static final String OUTPUT_BNE = "bne";
	
	// outputs
	private static String output_keys[] = {
		OUTPUT_LI, 
		OUTPUT_JUMP, 
		OUTPUT_REG_DST, 
		OUTPUT_ALU_SRC, 
		OUTPUT_MEM_TO_REG,
		OUTPUT_REG_WRITE,
		OUTPUT_MEM_READ, 
		OUTPUT_MEM_WRITE, 
		OUTPUT_BRANCH, 
		OUTPUT_ALU_OP_1, 
		OUTPUT_ALU_OP_0,
		OUTPUT_BNE
	};
	
	private Map<String, Boolean> current_outputs;
	
	public ControlUnit() {
		super();
		
		this.current_outputs = new HashMap<String, Boolean>();
		this.current_opcode = new BitData(Instruction.OPCODE_SIZE);
		
		for(String key :output_keys) {
			current_outputs.put(key, false);
		}
	}
	
	public int processOpcode(boolean[] opcode) throws ControlUnitException {
		
		if(opcode.length == Instruction.OPCODE_SIZE) {
			resetOutputs();
			setCurrentOutput(opcode);
			
			//000000
			boolean r_format = !opcode[0] && !opcode[1] && !opcode[2] && !opcode[3] && !opcode[4] && !opcode[5];
			//100011
			boolean lw = opcode[0] && !opcode[1] && !opcode[2] && !opcode[3] && opcode[4] && opcode[5];
			//101011
			boolean sw = opcode[0] && !opcode[1] && opcode[2] && !opcode[3] && opcode[4] && opcode[5];
			//000100
			boolean beq = !opcode[0] && !opcode[1] && !opcode[2] && opcode[3] && !opcode[4] && !opcode[5];
			//000010
			boolean jump = !opcode[0] && !opcode[1] && !opcode[2] && !opcode[3] && opcode[4] && !opcode[5];
			//001111
			boolean li = !opcode[0] && !opcode[1] && opcode[2] && opcode[3] && opcode[4] && opcode[5];
			//000101
			boolean bne = !opcode[0] && !opcode[1] && !opcode[2] && opcode[3] && !opcode[4] && opcode[5];
			
			if(!r_format && !lw && !sw && !beq && !jump && !li && !bne) {
				throw new ControlUnitException("Opcode not recognized.");
			}
			else {
				if(r_format) {
					current_outputs.replace(OUTPUT_REG_DST, true);
				}
				if(lw || sw || li) {
					current_outputs.replace(OUTPUT_ALU_SRC, true);
				}
				if(lw) {
					current_outputs.replace(OUTPUT_MEM_TO_REG, true);
				}
				if(li) {
					current_outputs.replace(OUTPUT_LI, true);
				}
				if(r_format || lw || li) {
					current_outputs.replace(OUTPUT_REG_WRITE, true);
				}
				if(lw) {
					current_outputs.replace(OUTPUT_MEM_READ, true);
				}
				if(sw) {
					current_outputs.replace(OUTPUT_MEM_WRITE, true);
				}
				if(beq) {
					current_outputs.replace(OUTPUT_BRANCH, true);
				}
				if(r_format) {
					current_outputs.replace(OUTPUT_ALU_OP_1, true);
				}
				if(beq || bne) {
					current_outputs.replace(OUTPUT_ALU_OP_0, true);
				}
				if(bne) {
					current_outputs.replace(OUTPUT_BNE, true);
				}
				if(jump) {
					current_outputs.replace(OUTPUT_JUMP, true);
				}
			}
			
			if(r_format) return Instruction.FORMAT_R;
			else if(lw) return Instruction.FORMAT_I;
			else if(sw) return Instruction.FORMAT_I;
			else if(beq) return Instruction.FORMAT_I;
			else if(jump) return Instruction.FORMAT_J;
			else if(li) return Instruction.FORMAT_I;
			else if(bne) return Instruction.FORMAT_I;
			else throw new ControlUnitException("Opcode not recognized.");
			
		}
		else {
			throw new ControlUnitException("Invalid opcode! Missing bits");
		}
	}
	
	public void printOutputs() {
		for(String key :current_outputs.keySet()) {
			System.out.println(key + " = " + current_outputs.get(key));
		}
	}
	
	public void resetOutputs() {
		for(String key :current_outputs.keySet()) {
			current_outputs.replace(key, false);
		}
	}
	
	public boolean getOutput(String name) {
		return current_outputs.get(name);
	}
	
	private void setCurrentOutput(boolean[] opcode) {
		if(opcode.length == Instruction.OPCODE_SIZE) {
			for(int i = 0; i < opcode.length; i++) {
				if(opcode[i]) {
					this.current_opcode.set(i);
				}
			}
		}
	}

	@Override
	public String toString() {
		String opcode = "";
		String outputs = "";
		
		for(int i = Instruction.OPCODE_SIZE - 1; i >= 0 ; i--) {
			opcode = opcode + current_opcode.getCharBit(i) + " ";
		}
		
		for(String key :this.current_outputs.keySet()) {
			outputs = outputs + "  - " + key + " = " + this.current_outputs.get(key) + "\n";
		}
		
		return 
			"Control Unit:\n" +
			"- opcode: " + opcode + "\n" +
			"- outputs:\n" + outputs;
	}
	
	
	
	
	
	
}
