 package simulator; 
/**
 * Abstract class Event - write a description of the class here
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
abstract class Event {

    private long time;
    
    /**
     * Create an event due to occur at the given time.
     */
    public Event(long systemTime) { this.time=systemTime;}
    
    /**
     * Obtain the system time at which this event is due to occur.
     */
    public long getTime() { return time; }
    
    /**
     * Process the event.
     */
    public abstract void process();
    
    public String toString() { return "Event("+getTime()+")"; }
}
