package simulator; 
/**
 * Abstract representation of a block of program code describing a cpu burst.
 * 
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
public class CPUInstruction extends Instruction {

    private int burstRemaining;
    
    public CPUInstruction(int duration) { super(duration); burstRemaining = duration; }
    
    /**
     * Obtain the amount of execution time required to complete this cpu burst.
     */
    int getBurstRemaining() { return burstRemaining; }
    
    /**
     * Simulate execution of cpu burst for <code>timeUnits</code> time.
     * 
     * If this cpu burst can complete in the given time, the method returns the quantity
     * of unused time units.
     * If this burst cannot complete in the given time then the method returns 
     * a -ve value, n,  where -n represents the amount of burst time remaining.
     * 
     * @return <code>timeUnits-this.getBurstRemaining()</code>.
     */
    int execute(int timeUnits){ 
        int remainder = timeUnits-burstRemaining;
        if (remainder<=0) {
            burstRemaining=Math.abs(remainder);
        }
        return remainder;
    }
        
    /**
     * Simulate execution of cpu burst to completion.
     * <p>
     * The method returns the quantity of time units consumed.
     * 
     * @return <code>this.getDuration()</code>.
     */
    int execute() {
		final int period = this.burstRemaining;
		this.execute(this.burstRemaining);
        return period;
    }
    
    /**
     * Return a string representation of the form "CPU <duration>".
     */
    public String toString() { return "CPU "+this.getDuration(); }
}
