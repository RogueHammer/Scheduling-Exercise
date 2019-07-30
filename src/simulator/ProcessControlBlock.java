package simulator; 
/**
 * Abstract Description of Process Control Block used by kernel and simulator.
 * 
 * @author Stephan Jamieson
 * @version 8/3/2015
 */
public interface ProcessControlBlock {

    /**
     * Possible process states.
     */
    enum State { WAITING, READY, RUNNING, TERMINATED };

    /**
     * Obtain process ID.
     */
    int getPID();

    /**
     * Obtain program name.
     * 
     */
    String getProgramName();
    
    /**
     * Obtain process priority();
     */
    int getPriority();
    
    /**
     * Set process priority(), returning the old value.
     */
    int setPriority(int value);
    
    /**
     * Obtain current program 'instruction'.
     */
    Instruction getInstruction();
    
    
    /**
     * Determine if there are any more instructions.
     */
    boolean hasNextInstruction();
    
    /**
     * Advance to next instruction.
     */
    void nextInstruction();
    
    /**
     * Obtain process state.
     */
    State getState();
    
    /**
     * Set process state.
     * Requires <code>getState()!=State.TERMINATED</code>.
     */
    void setState(State state);
    
    /**
     * Obtain a String representation of the PCB of the form '{pid(&lt;pid&gt;), state(&lt;state&gt;), name(&lt;program name&gt;)}'.
     */
    String toString();

}
