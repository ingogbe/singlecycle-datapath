package model;

import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;

import exception.ALUControlUnitException;
import exception.ArithmeticLogicUnitException;
import exception.BarrelExtensorException;
import exception.BarrelShifterException;
import exception.BitDataException;
import exception.ControlUnitException;
import exception.DataMemoryException;
import exception.FileException;
import exception.InstructionException;
import exception.InstructionMemoryException;
import exception.MultiplexerException;
import exception.RegistersException;

public class Datapath {
	
	BitData programCounter;
	InstructionMemory instructionMemory;
	ArithmeticLogicUnit alu_pcPlusFour;
	BarrelShifter shiftLeft2_instruction;
	ControlUnit controlUnit;
	Multiplexer multiplexer_instruction;
	Registers registers;
	BarrelExtensor signExtensor;
	Multiplexer multiplexer_ext_reg;
	ALUControlUnit aluControl;
	ArithmeticLogicUnit alu_zero;
	BarrelShifter shiftLeft2_ext;
	ArithmeticLogicUnit alu_pc_ext;
	Multiplexer multiplexer_beq;
	Multiplexer multiplexer_jump;
	DataMemory memory;
	Multiplexer multiplexer_alu_mem;
	Multiplexer multiplexer_jr;
	Multiplexer multiplexer_li_writeData;
	Multiplexer multiplexer_li_writeRegister;
	
	private BitData four;
	
	public Datapath () throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, FileException, IOException, InstructionMemoryException, InstructionException, BitDataException, ArithmeticLogicUnitException, MultiplexerException, BarrelShifterException, BarrelExtensorException, RegistersException, ControlUnitException, ALUControlUnitException, DataMemoryException{
		super();
		
		this.four = new BitData("00000000000000000000000000000100");
		
		this.programCounter = new BitData(32);
		this.instructionMemory = new InstructionMemory("Memory 1");
		System.out.println(instructionMemory);
		this.alu_pcPlusFour = new ArithmeticLogicUnit("ALU PC + 4", ArithmeticLogicUnit.SIGNAL_ADD);
		this.alu_pcPlusFour.addInputSignal(ArithmeticLogicUnit.SIGNAL_ADD, ArithmeticLogicUnit.OPERATION_ADD);
		this.controlUnit = new ControlUnit("Control 1");
		this.registers = new Registers("Registers memory");
		this.multiplexer_li_writeRegister = new Multiplexer("li writeRegister mux", 1);
		this.multiplexer_instruction = new Multiplexer("instruction mux", 1);
		this.shiftLeft2_instruction = new BarrelShifter("shift left 2 instruction addr");
		this.signExtensor = new BarrelExtensor("const extensor 16->32");
		this.multiplexer_ext_reg = new Multiplexer("extensor-register mux", 1);
		this.aluControl = new ALUControlUnit("ALU Zero Control");
		this.alu_zero = new ArithmeticLogicUnit("ALU Zero");
		this.shiftLeft2_ext = new BarrelShifter("shift left 2 extensor output");
		this.alu_pc_ext = new ArithmeticLogicUnit("ALU PC+4+extensor_output", ArithmeticLogicUnit.SIGNAL_ADD);
		this.alu_pc_ext.addInputSignal(ArithmeticLogicUnit.SIGNAL_ADD, ArithmeticLogicUnit.OPERATION_ADD);
		this.multiplexer_beq = new Multiplexer("beq mux", 1);
		this.multiplexer_jr = new Multiplexer("jr mux", 1);
		this.multiplexer_jump = new Multiplexer("jump mux", 1);
		this.memory = new DataMemory("Data memory 1");
		this.multiplexer_alu_mem = new Multiplexer("alu_zero data_memory mux", 1);
		this.multiplexer_li_writeData = new Multiplexer("li writeData mux", 1);
		
	}
	
	public void execute() throws InstructionMemoryException, InstructionException, ControlUnitException, MultiplexerException, ArithmeticLogicUnitException, BarrelShifterException, BarrelExtensorException, RegistersException, BitDataException, ALUControlUnitException, DataMemoryException{
		
		while(true){
			//Load instruction from instructon memory 'programcounter' address
			Instruction currentInstruction = this.instructionMemory.getInstruction(this.programCounter);
			System.out.println(currentInstruction);
			
			//Increments 'programcounter' in four
			BitData newProgramCounter = alu_pcPlusFour.execute(this.programCounter, this.four);
			System.out.println("pc+4: " + newProgramCounter + "\n");
			
			//Set instruction type and process control unit signals
			currentInstruction.setType(this.controlUnit.processOpcode(currentInstruction.getOpcode()));
			System.out.println(this.controlUnit);
			System.out.println(currentInstruction);
			
			//Set read register 1 and 2 address in registers
			this.registers.setReadRegister1(BitData.booleanToBitData(currentInstruction.getRS()));
			this.registers.setReadRegister2(BitData.booleanToBitData(currentInstruction.getRT()));
			System.out.println("set read reg1 address: " + BitData.booleanToBitData(currentInstruction.getRS()));
			System.out.println("set read reg1 address: " + BitData.booleanToBitData(currentInstruction.getRT()) + "\n");
			
			//Choose between RS and RT to use with input in multiplexer_instruction using LI signal
			this.multiplexer_li_writeRegister.addInput(new BitData("1"), BitData.booleanToBitData(currentInstruction.getRT()));
			this.multiplexer_li_writeRegister.addInput(new BitData("0"), BitData.booleanToBitData(currentInstruction.getRS()));
			boolean li_signal[] = {this.controlUnit.getOutput(ControlUnit.OUTPUT_LI)};
			this.multiplexer_li_writeRegister.setSignal(li_signal);
			System.out.println(this.multiplexer_li_writeRegister + "\n");
			
			//Choose between multiplexer_writeRegister output and RD to use with input to write register address
			this.multiplexer_instruction.addInput(new BitData("0"), this.multiplexer_li_writeRegister.getOutput());
			this.multiplexer_instruction.addInput(new BitData("1"), BitData.booleanToBitData(currentInstruction.getRD()));
			boolean regDst_signal[] = {this.controlUnit.getOutput(ControlUnit.OUTPUT_REG_DST)};
			this.multiplexer_instruction.setSignal(regDst_signal);
			System.out.println(this.multiplexer_instruction + "\n");
			
			//Set write register address
			this.registers.setWriteRegister1(multiplexer_instruction.getOutput());
			System.out.println(this.registers);
			
			//Shift left 2 the first 25 bits from instruction
			//And Add last four bits from pc+4 to complete 32 bits
			this.shiftLeft2_instruction.setInput(BitData.booleanToBitData(currentInstruction.getAddr()), 2);
			System.out.println(this.shiftLeft2_instruction + "\n");
			BitData shiftLeft2_instruction_pc4_output = new BitData(32);
			for(int i = 0; i < 4; i++){
				shiftLeft2_instruction_pc4_output.set(i, newProgramCounter.get(i));
			}
			shiftLeft2_instruction_pc4_output.setBits(4, BitData.bitDataToBoolean(this.shiftLeft2_instruction.getOutput()));
			System.out.println("shift left 2 + pc4: " + shiftLeft2_instruction_pc4_output + "\n");
			
			//Extend const from 16 bits to 32 bits
			this.signExtensor.setInput(BitData.booleanToBitData(currentInstruction.getConst()), 32);
			BitData signExtensor_output = this.signExtensor.getOutput();
			System.out.println(this.signExtensor + "\n");
			
			//Choose between signExtensor output and readRegister2 from registers to use with input ALU Zero
			this.multiplexer_ext_reg.addInput(new BitData("1"), signExtensor_output);
			this.multiplexer_ext_reg.addInput(new BitData("0"), this.registers.getReadData2());
			boolean aluScr_signal[] = {this.controlUnit.getOutput(ControlUnit.OUTPUT_ALU_SRC)};
			this.multiplexer_ext_reg.setSignal(aluScr_signal);
			BitData multiplexer_ext_reg_output = multiplexer_ext_reg.getOutput();
			System.out.println(this.multiplexer_ext_reg + "\n");
			
			//Set funct to alucontrol and control signals
			this.aluControl.setFunct(currentInstruction.getFunct());
			this.aluControl.setControlSignal(this.controlUnit.getOutput(ControlUnit.OUTPUT_ALU_OP_1), this.controlUnit.getOutput(ControlUnit.OUTPUT_ALU_OP_0), this.controlUnit.getOutput(ControlUnit.OUTPUT_LI));
			System.out.println(this.aluControl);
			
			//Set write signal in register
			this.registers.setWriteSignal(this.controlUnit.getOutput(ControlUnit.OUTPUT_REG_WRITE) && !this.aluControl.getJR());
			System.out.println(this.registers);
			
			//Set aluzero signal and execute
			this.alu_zero.setSignal(BitData.bitDataToBoolean(this.aluControl.getOutput()));
			BitData alu_zero_output = this.alu_zero.execute(this.registers.getReadData1(), multiplexer_ext_reg_output);
			System.out.println(this.alu_zero);
			
			//Shift left 2 const extended
			this.shiftLeft2_ext.setInput(signExtensor_output, 2);
			System.out.println(this.shiftLeft2_ext + "\n");
			
			//Add pc+4 with shifted2 const
			BitData alu_pc_ext_output = this.alu_pc_ext.execute(newProgramCounter, this.shiftLeft2_ext.getOutput());
			System.out.println(this.alu_pc_ext);
			
			//Choose between pc+4 and jumped address
			this.multiplexer_beq.addInput(new BitData("1"), alu_pc_ext_output);
			this.multiplexer_beq.addInput(new BitData("0"), newProgramCounter);
			boolean mux_beq_signal[] = {(this.controlUnit.getOutput(ControlUnit.OUTPUT_BNE) && !this.alu_zero.getZero()) || (this.alu_zero.getZero() && this.controlUnit.getOutput(ControlUnit.OUTPUT_BRANCH))};
			this.multiplexer_beq.setSignal(mux_beq_signal);
			System.out.println(this.multiplexer_beq + "\n");
			
			//Choose between muxbeq and readedata1 from register
			this.multiplexer_jr.addInput(new BitData("0"), this.multiplexer_beq.getOutput());
			this.multiplexer_jr.addInput(new BitData("1"), this.registers.getReadData1());
			boolean mux_jr_signal[] = {(this.aluControl.getJR())};
			this.multiplexer_jr.setSignal(mux_jr_signal);
			System.out.println(this.multiplexer_jr + "\n");
			
			//Choose between muxjr and shiftedleft instruction with pc4
			this.multiplexer_jump.addInput(new BitData("0"), this.multiplexer_jr.getOutput());
			this.multiplexer_jump.addInput(new BitData("1"), shiftLeft2_instruction_pc4_output);
			boolean mux_jump_signal[] = {this.controlUnit.getOutput(ControlUnit.OUTPUT_JUMP)};
			this.multiplexer_jump.setSignal(mux_jump_signal);
			System.out.println(this.multiplexer_jump + "\n");
			
			//set memory signals, address and writeData
			this.memory.setWriteSignal(this.controlUnit.getOutput(ControlUnit.OUTPUT_MEM_WRITE));
			this.memory.setReadSignal(this.controlUnit.getOutput(ControlUnit.OUTPUT_MEM_READ));
			this.memory.setAddress(alu_zero_output);
			this.memory.writeData(this.registers.getReadData2());
			System.out.println(this.memory);
			
			//Choose between memory read data and alu_zero output
			this.multiplexer_alu_mem.addInput(new BitData("1"), this.memory.readData());
			this.multiplexer_alu_mem.addInput(new BitData("0"), alu_zero_output);
			boolean mux_alu_mem_signal[] = {this.controlUnit.getOutput(ControlUnit.OUTPUT_MEM_TO_REG)};
			this.multiplexer_alu_mem.setSignal(mux_alu_mem_signal);
			System.out.println(this.multiplexer_alu_mem + "\n");
			
			//Choose between mux_alu_mem and mux_ext_reg
			this.multiplexer_li_writeData.addInput(new BitData("0"), this.multiplexer_alu_mem.getOutput());
			this.multiplexer_li_writeData.addInput(new BitData("1"), multiplexer_ext_reg_output);
			boolean mux_li_writeData_signal[] = {this.controlUnit.getOutput(ControlUnit.OUTPUT_LI)};
			this.multiplexer_li_writeData.setSignal(mux_li_writeData_signal);
			System.out.println(this.multiplexer_li_writeData + "\n");
			
			//write data to register
			this.registers.writeData(this.multiplexer_li_writeData.getOutput());
			System.out.println(this.registers);
			
			this.programCounter = this.multiplexer_jump.getOutput();
			System.out.println("new pc: " + this.programCounter);
			
			System.out.println("------------------------------------------");
		}
	}
	
	
	
	
}
