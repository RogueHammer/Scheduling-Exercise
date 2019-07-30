package programgenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//
import simulator.Instruction;
/**
 * Write a description of class ProgramGenerator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ProgramGenerator {
    private CPUGenerator cpuGen;
    private IOGenerator ioGen;

    private String name;    
    private final List<Instruction> instructions; 
    private int totalTime;
    
    /**
     * @Param numInstructions - an odd number integer.
     */
    public ProgramGenerator(String name, CPUGenerator cpuGen, IOGenerator ioGen, int numInstructions) {
        assert(numInstructions%2==1);

        this.name=name;
        this.totalTime=0;
        this.cpuGen=cpuGen;
        this.ioGen=ioGen;
        this.instructions=new ArrayList<Instruction>(); 

        make(cpuGen);
        for (int i=1; i<numInstructions; i=i+2) {
            make(ioGen);
            make(cpuGen);
        }
    }
            
    public int getTotalTime() { return this.totalTime; }
    public int getSize() { return instructions.size(); }
    public String getName() { return this.name; }
    
    private void make(InstructionGenerator instGen) {
        Instruction instruction = instGen.make();
        instructions.add(instruction);
        totalTime=totalTime+instruction.getDuration();
    }
    
    public void make() {
        make(ioGen);
        make(cpuGen);
    }
    
    public List<String> getText() {
        final List<String> result = new ArrayList<String>();
        result.add("# Program name: "+this.getName());
        result.add("# "+cpuGen);
        result.add("# "+ioGen);
        for(Instruction instruction : instructions) {
            result.add(instruction.toString());
        }
        return result;
    }
    
    public static void main(String args[]) {
        try {
            final Scanner in = new Scanner(System.in);

            System.out.print("CPU Bounds (lambda, min burst, max burst): ");
            final Scanner cpuBounds = new Scanner(in.nextLine());
            cpuBounds.useDelimiter("\\s*,\\s*");
            final CPUGenerator cpuGen = new CPUGenerator(new BoundedExpRNG(cpuBounds.nextDouble()), cpuBounds.nextInt(), cpuBounds.nextInt());

            System.out.print("IO Bounds (lambda, min burst, max burst): ");
            final Scanner ioBounds = new Scanner(in.nextLine());
            ioBounds.useDelimiter("\\s*,\\s*");
            
            System.out.print("Device identifiers (<integer>, ..., <integer>): ");
            final Scanner deviceIDs = new Scanner(in.nextLine());
            deviceIDs.useDelimiter("\\s*,\\s*");
            final List<Integer> deviceList = new ArrayList<Integer>();
            while (deviceIDs.hasNextInt()) { deviceList.add(deviceIDs.nextInt()); }
            
            final IOGenerator ioGen = new IOGenerator(new BoundedExpRNG(ioBounds.nextDouble()), deviceList.toArray(new Integer[0]), ioBounds.nextInt(), ioBounds.nextInt());
            
            System.out.print("Length (number of instructions - must be odd number): ");
            final int length = in.nextInt();
            in.nextLine();
            
            System.out.print("Program name: ");
            final String name = in.nextLine();
            System.out.println(); 
            // Hold your breath, here we go...
            final ProgramGenerator progGen = new ProgramGenerator(name, cpuGen, ioGen, length);
            
            final BufferedWriter writer = new BufferedWriter(new FileWriter(name));
            for(String line : progGen.getText()) {
                System.out.println(line);
                writer.write(line+"\n");
            }
            writer.close();
        }
        catch (Exception Excep){
            System.out.println("Oops, something broke.");
        }
    }
}
