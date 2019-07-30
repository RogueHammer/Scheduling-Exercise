package simulator; 
/**
 * Interface for a kernel that handles interrupts
 * such as timeouts and completion of device I/O operations.
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
public interface InterruptHandler {

    /**
     * Identifies a time_out interrupt. 
     * The PID of the affected process is provided as a parameter - generally, it should correspond 
	 * to that of the process currently executing!
     */
    final static int TIME_OUT = 0;
    
    /**
     * Identifies completion of a device I/O request. 
     * Device ID and Process ID are required parameters.
     */
    final static int WAKE_UP = 1;
    
    /**
     * Invoke the interrupt handler, providing the interrupt type and zero or more arguments.
     */
    public void interrupt(int interruptType, Object... varargs);
    
}
