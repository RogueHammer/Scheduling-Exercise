/**
 * PCB Implementation for OS Assignment 1
 *
 * @author Steven Mare	
 * @version 02 May 2018
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import simulator.ProcessControlBlock;
import simulator.CPUInstruction;
import simulator.IOInstruction;
import simulator.Instruction;

public class ProcessControlBlockImpl implements ProcessControlBlock{

    private static int counter = 1;
	private int PID;
	private String programName;
	private int priority;
	private State state;
    private List<Instruction> instructions;

	public ProcessControlBlockImpl(){
        instructions = new ArrayList<Instruction>();
        state = State.READY;
        PID = counter++;
	}

	public int getPID(){
		return PID;
	}

    public String getProgramName(){
    	return programName;
    }
    
    public int getPriority(){
    	return priority;
    }
    
    public int setPriority(int value){
    	int temp = priority;
    	priority = value;
    	return temp;
    }
    
    public Instruction getInstruction(){
    	return instructions.get(0);
    }
    
    public boolean hasNextInstruction(){
        return instructions.size()>1;
    }

    public void nextInstruction(){
        instructions.remove(0);
    }

    public State getState(){
    	return state;
    }

    public void setState(State state){
        if(getState()!=State.TERMINATED){
            this.state = state;
        }
    	
    }
    
    public String toString(){
        return ("process(pid="+getPID()+", state="+getState()+", name=\""+getProgramName()+"\")");
    }

    public static ProcessControlBlock loadProgram(String filename) throws IOException, FileNotFoundException{
        ProcessControlBlockImpl pcbi = new ProcessControlBlockImpl();
        pcbi.programName = filename;
        final BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        while(line!=null){
            Scanner scan = new Scanner(line);
            String type = scan.next();

            if(type.equals("CPU")){
                int cpuburst = scan.nextInt();
                CPUInstruction cpuinst = new CPUInstruction(cpuburst);
                pcbi.instructions.add(cpuinst);
            }
            else if(type.equals("IO")){
                int ioburst = scan.nextInt();
                int ioid = scan.nextInt();
                IOInstruction ioinst = new IOInstruction(ioburst,ioid);
                pcbi.instructions.add(ioinst);
            }

            line = reader.readLine();
        }
        reader.close();

        return pcbi;
    }
}