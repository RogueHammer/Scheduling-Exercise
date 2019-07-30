 package simulator; 
/**
 * A WakeUp event occurs when an I/O operation completes.
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
class WakeUpEvent extends Event {
    
    private int deviceID;
    private int processID;
    private EventHandler<WakeUpEvent> handler;
    
    /**
     * Create a WakeUpEvent for the given process waiting on an I/O operation on the given device.
     */
    public WakeUpEvent(long systemTime, int deviceID, int processID, EventHandler<WakeUpEvent> handler) {
        super(systemTime);
        this.deviceID=deviceID;
        this.processID=processID;
        this.handler=handler;
    }
    
    /**
     * Obtain the I/O device.
     */
    public int getDeviceID() { return deviceID; }
    
    /**
     * Obtain the waiting process.
     */
    public int getProcessID() { return processID; }

    /**
     * Process this event.
     */
    public void process() {
        handler.process(this);
    }
    
    public String toString() { return "WakeUpEvent("+this.getTime()+", "+this.getDeviceID()+", "+this.getProcessID()+")"; }
        
}
