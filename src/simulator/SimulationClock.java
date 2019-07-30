package simulator; 
//
import java.util.HashMap;
import java.util.Map;
/**
 * Implementation of SystemTimer type.
 * 
 * @author Stephan Jamieson
 * @version 8/3/2015
 */
class SimulationClock implements EventHandler<TimeOutEvent>, SystemTimer {
    
    private final int SYSCALL_COST;
    private final int CSWITCH_COST;
    
    private Map<TimeOutEvent, InterruptHandler> pendingTimeouts;
    
    public void scheduleInterrupt(int timeUnits, InterruptHandler handler, Object... varargs) {
        Integer processID = (Integer)varargs[0];
        TimeOutEvent timeOut = new TimeOutEvent(timeUnits+systemTime, processID, this);
        pendingTimeouts.put(timeOut, handler);
        Config.getEventScheduler().schedule(timeOut);
    }
    
    public void cancelInterrupt(int processID) {
        TimeOutEvent event = null;
        for(TimeOutEvent candidate : pendingTimeouts.keySet()) {
            if (candidate.getProcessID()==processID) {
                event = candidate;
                break;
            }
        }
        assert(event!=null);
        pendingTimeouts.remove(event);
        Config.getEventScheduler().cancel(event);
    }
    
    public void process(TimeOutEvent event) {
        InterruptHandler handler = pendingTimeouts.get(event);
        pendingTimeouts.remove(event);

        TRACE.INTERRUPT(InterruptHandler.TIME_OUT, Config.getCPU().getCurrentProcess());
        Config.getSimulationClock().logInterrupt();
        handler.interrupt(InterruptHandler.TIME_OUT, event.getProcessID());
        TRACE.INTERRUPT_END();
    }
    
    
    private long systemTime;
    private long kernelTime;
    private long userTime;    
    
    public SimulationClock(int sysCallCost, int cSwitchCost) {
        this.systemTime = 0;
        this.kernelTime = 0;
        this.userTime = 0;
        pendingTimeouts = new HashMap<TimeOutEvent, InterruptHandler>();

        this.SYSCALL_COST=sysCallCost;
        this.CSWITCH_COST=cSwitchCost;
    }
    
    void logContextSwitch() {
        this.advanceKernelTime(this.CSWITCH_COST);
    }
    
    void logInterrupt() {
        this.advanceKernelTime(this.SYSCALL_COST);
    }
    
    void logSystemCall() {
        this.advanceKernelTime(this.SYSCALL_COST);
    }
    
    public void advanceKernelTime(long time) {
        assert(time>=0);
        systemTime+=time;
        kernelTime+=time;
    }
    
    public void advanceSystemTime(long time) {
        assert(time>=0);
        systemTime+=time;
    }
    
    public void advanceUserTime(long time) {
        assert(time>=0);
        systemTime+=time;
        userTime+=time;
    }
   
    public void setSystemTime(long time) {
        assert(time>=0);
        systemTime=time;
        userTime=0;
        kernelTime=0;
    }
    
    
    public long getSystemTime() { return systemTime; }
    public long getKernelTime() { return kernelTime; }
    public long getUserTime() { return userTime; }
    public long getIdleTime() { return systemTime-(userTime+kernelTime); }
   
    
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("System time: "+this.getSystemTime()+"\n");
        stringBuilder.append("Kernel time: "+this.getKernelTime()+"\n");
        stringBuilder.append("User time: "+this.getUserTime()+"\n");
        stringBuilder.append("Idle time: "+(this.getSystemTime()-(this.getKernelTime()+this.getUserTime())));
        return stringBuilder.toString();
    }
        
}
