package simulator; 
/**
 * Interface to the simulated kernel.
 * 
 * Provides two sub interfaces: a system call interface, and an interrupt handler. See the documentation for those interfaces for details.
 * 
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
public interface Kernel extends SystemCall, InterruptHandler { }
