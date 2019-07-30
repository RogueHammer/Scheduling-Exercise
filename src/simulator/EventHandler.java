package simulator; 
/**
 * 
 * 
 * @author Stephan Jamieson 
 * @version 8/3/15
 */
interface EventHandler<E extends Event> {

    void process(E event);
}
