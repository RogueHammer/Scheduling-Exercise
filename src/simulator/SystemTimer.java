package simulator; 
/**
 * A SystemTimer (i) provides support for simulated time, and (ii) support for
 * the setting of timeout interrupts by kernel code. 
 * 
 * @author Stephan Jamieson 
 * @version 8/3/2015
 */
public interface SystemTimer  {

    /** 
     * Obtain the current system time.
     */
    long getSystemTime();
    
    /**
     * Obtain the amount of time the CPU has been idle.
     */
    long getIdleTime();

    /**
     * Obtain the amount of time the system has spent executing in user space.
     */
    long getUserTime();

    /**
     * Obtain the amount of time the system has spent executing in kernel space.
     */
    long getKernelTime();

    
    /**
     * Schedule a timer interrupt for <code>timeUnits</code> time units in the future.
	 * The given handler receives a call back when it occurs.
     */
    void scheduleInterrupt(int timeUnits, InterruptHandler handler, Object... varargs);
    
    /**
     * Cancel timer interrupt for given process ID.
     */
    void cancelInterrupt(int processID);
    
    
    
    
}
