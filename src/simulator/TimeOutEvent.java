 package simulator; 
/**
 * A timeout event is used to flag the end of the current execution time slice.
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
class TimeOutEvent extends Event {

    private int processID;
    private EventHandler<TimeOutEvent> handler;
    
    /**
     * Create a TimeOut event to mark the end of the execution timeslice for the given process.
     */
    public TimeOutEvent(long systemTime, int processID, EventHandler<TimeOutEvent> handler) {
        super(systemTime);
        this.processID=processID;
        this.handler = handler;
    }
    
    /**
     * Obtain the process to switched out as a result of this execution timeout.
     */
    public int getProcessID() { return processID; }


    public void process() {
        handler.process(this);
    }
    
    public String toString() { return "TimeOutEvent("+this.getTime()+", "+this.getProcessID()+")";}
}

