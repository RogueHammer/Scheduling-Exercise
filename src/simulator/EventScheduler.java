 package simulator; 
/**
 * Implementation of EventScheduler
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
class EventScheduler {

    private EventQueue queue;
    
    public EventScheduler() {
        this.queue = new EventQueue();
    }
    
        
    public void schedule(final Event event) { 
        queue.add(event); 
        TRACE.PRINTF(32, "Time: %010d Simulator: add %s\n", Config.getSystemTimer().getSystemTime(), event.toString()); 
    }
    
    public void cancel(final Event event) { 
        queue.remove(event); 
        TRACE.PRINTF(32, "Time: %010d Simulator: cancel %s\n", Config.getSystemTimer().getSystemTime(), event.toString());  
    }
    
    public void run() {
        assert(Config.getSimulationClock()!=null & Config.getCPU()!=null);
    
        while (!(queue.isEmpty() && Config.getCPU().isIdle())) {
            while (!queue.isEmpty() && queue.peek().getTime()<=Config.getSimulationClock().getSystemTime()) {
                final Event event = queue.poll();
                TRACE.PRINTF(32, "Time: %010d Simulator: process %s\n", Config.getSystemTimer().getSystemTime(), event.toString()); 
                event.process();
            }
            if (!queue.isEmpty()) {
                Config.getCPU().execute((int)(queue.peek().getTime()-Config.getSimulationClock().getSystemTime()));
            }
            else {
                Config.getCPU().execute();
            }
        }
	}
}
