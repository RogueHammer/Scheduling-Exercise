package simulator; 
/**
 * Abstract representation of a block of program code describing an I/O burst.
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
public class IOInstruction extends Instruction {

    private int deviceID;
    
    /**
     * Create an IOInstruction of the given duration for the given process and I/O device.
     */
    public IOInstruction(int duration, int deviceID) {
        super(duration);
        this.deviceID = deviceID;
    }
    
    /**
     * Get the ID of the I/O device.
     */
    public int getDeviceID() { return deviceID; }
    
    /**
     * Return a string representation of the form "IO <duration> <device id>".
     */
    public String toString() {
        return "IO "+this.getDuration()+" "+this.getDeviceID();
    }
}
