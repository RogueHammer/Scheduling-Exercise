 package simulator; 
//
import java.util.Comparator;
import java.util.PriorityQueue;
/**
 * Ordered Event queue for discrete event simulator.
 * Queue ordered by event time. 
 * Events may share the same time.
 * 
 * @author Stephan Jamieson
 * @version 08/3/2015
 */
class EventQueue {

    private PriorityQueue<Event> queue;
    
    /**
     * Create an empty EventQueue.
     */
    public EventQueue() { this.queue=new PriorityQueue<Event>(11, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                if (e1.getTime()<e2.getTime()) return -1;
                else if (e1.getTime()>e2.getTime()) return 1;
                else return 0;
            }
        }); 
    }

    /**
     * Insert an event in the queue.
     */
    public void add(Event e) { queue.add(e);}
    
    /**
     * View event at front of queue. 
     */
    public Event peek() { return queue.peek();}

    /**
     * Remove event at front of queue.
     * 
     * @return the event at the front of the queue or <code>null</code>. 
     */
    public Event poll() { return queue.poll(); }

    
    /**
     * Remove the given event from the queue.
     * @return true if item removed, false if not found.
     */
    public boolean remove(Event event) { return queue.remove(event); }
        
    /**
     * Determine if queue is empty.
     */
    public boolean isEmpty() { return queue.isEmpty(); }
}
