 package simulator; 
/**
 * An Execve event represents the creation of a program execution.
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
class ExecveEvent extends Event {

    private String progName;
    private Kernel kernel;
    private int priority;
    
    public ExecveEvent(long startTime, String progName, int priority, Kernel kernel) {
        super(startTime);
        this.priority=priority;
        this.progName=progName;
        this.kernel=kernel;
    }
        
    
    /**
     * Obtain the name of the program that must be run.
     */
    public String getProgramName() {
        return progName;
    }
    
    /**
     * Obtain the priority of the program that must be run.
     */
   int getPriority() { return priority; }
   
   public void process() {
       TRACE.SYSCALL(SystemCall.EXECVE, getProgramName());
       Config.getSimulationClock().logSystemCall();
       kernel.syscall(SystemCall.EXECVE, getProgramName(), getPriority());
       TRACE.SYSCALL_END();
    }
    
    public String toString() { return "ExecveEvent("+this.getTime()+", "+this.getProgramName()+"["+this.getPriority()+"])"; }

}
