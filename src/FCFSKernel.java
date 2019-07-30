import simulator.Config;
import simulator.IODevice;
import simulator.Kernel;
import simulator.ProcessControlBlock;
//
import java.io.FileNotFoundException;
import java.io.IOException;
//
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Concrete Kernel type
 *
 * @author Stephan Jamieson
 * @version 8/3/15
 */
public class FCFSKernel implements Kernel {


    private Deque<ProcessControlBlock> readyQueue;

    public FCFSKernel() {
		// Set up the ready queue.
        readyQueue = new ArrayDeque<ProcessControlBlock>();
    }

    private ProcessControlBlock dispatch() {
		// Perform context switch, swapping process
		// currently on CPU with one at front of ready queue.
		// If ready queue empty then CPU goes idle ( holds a null value).
		// Returns process removed from CPU.

        if(readyQueue.isEmpty()){
          return Config.getCPU().contextSwitch(null);
        }
        else{
          ProcessControlBlock nextpcb = readyQueue.pop();
          ProcessControlBlock lastpcb = Config.getCPU().contextSwitch(nextpcb);
          nextpcb.setState(ProcessControlBlock.State.RUNNING);
          return lastpcb;      
        }
	}



    public int syscall(int number, Object... varargs) {
        int result = 0;
        switch (number) {
             case MAKE_DEVICE:
                {
                    IODevice device = new IODevice((Integer)varargs[0], (String)varargs[1]);
                    Config.addDevice(device);
                }
                break;
             case EXECVE:
                {
                    ProcessControlBlock pcb = this.loadProgram((String)varargs[0]);
                    if (pcb!=null) {
                        // Loaded successfully.
						// Now add to end of ready queue.
						// If CPU idle then call dispatch.
                        readyQueue.add(pcb);
                        if(Config.getCPU().isIdle()){
                            dispatch();
                        }
                    }
                    else {
                        result = -1;
                    }
                }
                break;
             case IO_REQUEST:
                {
					// IO request has come from process currently on the CPU.
					// Get PCB from CPU.
					// Find IODevice with given ID: Config.getDevice((Integer)varargs[0]);
					// Make IO request on device providing burst time (varages[1]),
					// the PCB of the requesting process, and a reference to this kernel (so // that the IODevice can call interrupt() when the request is completed.
					//
					// Set the PCB state of the requesting process to WAITING.
					// Call dispatch().

                    ProcessControlBlock currentpcb = Config.getCPU().getCurrentProcess();
                    IODevice nextIO = Config.getDevice((Integer)varargs[0]);
                    nextIO.requestIO((Integer)varargs[1],currentpcb, this);
                    currentpcb.setState(ProcessControlBlock.State.WAITING);
                    dispatch();
                }
                break;
             case TERMINATE_PROCESS:
                {
					// Process on the CPU has terminated.
					// Get PCB from CPU.
					// Set status to TERMINATED.
                    // Call dispatch().
                    ProcessControlBlock currentpcb = Config.getCPU().getCurrentProcess();
                    currentpcb.setState(ProcessControlBlock.State.TERMINATED);
                    dispatch();

                }
                break;
             default:
                result = -1;
        }
        return result;
    }


    public void interrupt(int interruptType, Object... varargs){
        switch (interruptType) {
            case TIME_OUT:
                throw new IllegalArgumentException("FCFSKernel:interrupt("+interruptType+"...): this kernel does not suppor timeouts.");
            case WAKE_UP:
				// IODevice has finished an IO request for a process.
				// Retrieve the PCB of the process (varargs[1]), set its state
				// to READY, put it on the end of the ready queue.
				// If CPU is idle then dispatch().
                ProcessControlBlock currentpcb = (ProcessControlBlock)(varargs[1]);
                currentpcb.setState(ProcessControlBlock.State.READY);
                readyQueue.add(currentpcb);
                if(Config.getCPU().isIdle()){
                  dispatch();  
                }
                
                break;
            default:
                throw new IllegalArgumentException("FCFSKernel:interrupt("+interruptType+"...): unknown type.");
        }
    }

    private static ProcessControlBlock loadProgram(String filename) {
        try {
            return ProcessControlBlockImpl.loadProgram(filename);
        }
        catch (FileNotFoundException fileExp) {
            return null;
        }
        catch (IOException ioExp) {
            return null;
        }
    }
}
