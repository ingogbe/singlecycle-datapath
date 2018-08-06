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
import model.ALUControlUnit;
import model.ArithmeticLogicUnit;
import model.BarrelExtensor;
import model.BitData;
import model.ControlUnit;
import model.DataMemory;
import model.Datapath;
import model.Instruction;
import model.InstructionMemory;
import model.Multiplexer;
import model.Registers;

public class Main {
	
	
	public static void main(String[] args) {
		//test();
		try {
			Datapath dp = new Datapath();
			dp.execute();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException | FileException | IOException | InstructionMemoryException
				| InstructionException | BitDataException | ArithmeticLogicUnitException | MultiplexerException
				| BarrelShifterException | BarrelExtensorException | RegistersException | ControlUnitException
				| ALUControlUnitException | DataMemoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void test() {
		try {
			boolean signals[] = {true};
			Multiplexer mux = new Multiplexer("Mux test", signals);
			BitData input1 = new BitData("00000000000000000000000000000001");
			BitData signal1 = new BitData("0");
			BitData input2 = new BitData("00000000000000000000000000000011");
			BitData signal2 = new BitData("1");
			BitData input3 = new BitData("00000000000000000000000000000111");
			BitData signal3 = new BitData("1");
			mux.addInput(signal1 , input1);
			mux.addInput(signal2 , input2);
			mux.addInput(signal3 , input3);
			System.out.println(mux);
			
			InstructionMemory instr_mem = new InstructionMemory("instruction mem test");
			System.out.println(instr_mem);
			System.out.println("\nGet instruction 0 from memory:");
			BitData address = new BitData("00000000000000000000000000000000");
			Instruction instruction = instr_mem.getInstruction(address);
			System.out.println(instruction.toString());
			
			ControlUnit uc = new ControlUnit("UC Test");
			System.out.println("Processing instruction. Opcode = " + BitData.booleanToBitData(instruction.getOpcode()));
			instruction.setType(uc.processOpcode(instruction.getOpcode()));
			System.out.println(uc.toString());
			System.out.println("Instruction after opcode processed (has type)");
			System.out.println(instruction.toString());
			
			Registers regs = new Registers("reg test");
			regs.setReadRegister1(BitData.booleanToBitData(instruction.getRS()));
			regs.setReadRegister2(BitData.booleanToBitData(instruction.getRT()));
			regs.setWriteRegister1(BitData.booleanToBitData(instruction.getRD()));
			regs.setWriteSignal(uc.getOutput(ControlUnit.OUTPUT_REG_WRITE));
			System.out.println(regs);
			
			ArithmeticLogicUnit alu = new ArithmeticLogicUnit("alu test");
			BitData bd1 = new BitData("1110110");
			System.out.println("Input1: " + bd1);
			BitData bd2 = new BitData("1010111");
			System.out.println("Input2: " + bd2);
			alu.setSignal(ArithmeticLogicUnit.SIGNAL_ADD);
			System.out.println("Add   : " + alu.execute(bd1, bd2));
			alu.setSignal(ArithmeticLogicUnit.SIGNAL_AND);
			System.out.println("And   : " + alu.execute(bd1, bd2));
			alu.setSignal(ArithmeticLogicUnit.SIGNAL_OR);
			System.out.println("Or    : " + alu.execute(bd1, bd2));
			alu.setSignal(ArithmeticLogicUnit.SIGNAL_SUB);
			System.out.println("Sub   : " + alu.execute(bd1, bd2));
			
			BarrelExtensor extensor = new BarrelExtensor("extensor test");
			extensor.setInput(bd1, 32);
			System.out.println("Extending to 32 bits");
			System.out.println("Input : " + bd1);
			System.out.println("Output: " + extensor.getOutput());
			
			System.out.println("\nALU Control:");
			Instruction instruction2 = new Instruction("00001000000000000000000000001000");
			instruction2.setType(uc.processOpcode(instruction2.getOpcode()));
			System.out.println(instruction2);
			ALUControlUnit aluControl = new ALUControlUnit("alu control test");
			aluControl.setControlSignal(uc.getOutput(ControlUnit.OUTPUT_ALU_OP_1), uc.getOutput(ControlUnit.OUTPUT_ALU_OP_0), uc.getOutput( ControlUnit.OUTPUT_LI));
			System.out.println("Funct    : " + BitData.booleanToBitData(instruction2.getFunct()));
			System.out.println("Signal   : " + BitData.booleanToBitData(aluControl.getControlSignal()));
			aluControl.setFunct(instruction2.getFunct());
			System.out.println("Operation: " + aluControl.getOutput());
			System.out.println("JR       :  " + aluControl.getJR());
			
			DataMemory memory = new DataMemory("memory test");
			memory.setReadSignal(true);
			memory.setWriteSignal(true);
			System.out.println(memory);
			System.out.println("Setting address to: 00000000000000000000000000000100");
			memory.setAddress(new BitData("00000000000000000000000000000100"));
			System.out.println("Writing: 00000000000000000000000000111111");
			memory.writeData(new BitData("00000000000000000000000000111111"));
			System.out.println("Current address: " + memory.getAddress().toString(""));
			System.out.println("Read data from memory: " + memory.readData().toString(""));
			
			
		}
		catch (InstructionException e) {
			e.printStackTrace();
		}
		catch (ControlUnitException e) {
			e.printStackTrace();
		}
		catch (BitDataException e) {
			e.printStackTrace();
		}
		catch (MultiplexerException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} 
		catch (FileException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InstructionMemoryException e) {
			e.printStackTrace();
		} 
		catch (ArithmeticLogicUnitException e) {
			e.printStackTrace();
		} 
		catch (BarrelExtensorException e) {
			e.printStackTrace();
		} 
		catch (BarrelShifterException e) {
			e.printStackTrace();
		}
		catch (ALUControlUnitException e) {
			e.printStackTrace();
		} 
		catch (RegistersException e) {
			e.printStackTrace();
		}
		catch (DataMemoryException e) {
			e.printStackTrace();
		}
	}
}



