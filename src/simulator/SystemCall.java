package simulator; 
/**
 * System call interface for OS kernel.
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
public interface SystemCall {

    /**
     * Create a device. Device ID and name are required arguments.
     */
    final static int MAKE_DEVICE = 1;
    
    /**
     * Execute a program. Program name is a required argument. 
     * Process ID is returned when the call completes.
     */
    final static int EXECVE = 2;
    
    /**
     * Perform IO request. Device ID and IO burst duration are required arguments.
     */
    final static int IO_REQUEST = 3;
    
    /**
     * Terminate current process.
     */
    final static int TERMINATE_PROCESS = 4;
    
    
    /**
     *  Invoke the system call with the given number, providing zero or more arguments.
     */
    public int syscall(int number, Object... varargs);
    
    
}
