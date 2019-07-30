package simulator; 
/**
 * Controller for execution tracing. A bitwise integer value is used to 
 * represent the level of detail:
 * <p>
 * Bit 1: value 1: Trace context switches.</br>
 * Bit 2: value 2: Trace system call entry.</br>
 * Bit 3: value 4: Trace system call exit.</br>
 * Bit 4: value 8: Trace interrupt handler entry.</br>
 * Bit 5: value 16: Trace interrupt handler exit.</br>
 * Bit 6: value 32: Trace behind the scenes discrete event processing.
 * <p>
 * A value of say, 11, causes context switching, system call and interrupt handler entry to be traced. 
 
 * @author Stephan Jamieson
 * @version 12/3/2016
 */
public class TRACE {
	private final static int KERNEL = 10;
	private final static int SIMULATOR = 20;
	
    private TRACE() {}
    
    private static int traceLevel;
    
    /**
     * Obtain current trace level.
     */
    public static int GET_TRACE_LEVEL() { return traceLevel; }
    
    /**
     * Set the trace level with the given bitwise int value.
     */
    public static int SET_TRACE_LEVEL(int level) { 
        final int old = TRACE.traceLevel;
        TRACE.traceLevel=level;
        return old;
    }
    
    /**
     * If GET_TRACE_LEVEL()&level!=0 then perform System.out.printf(format, args) else do nothing. 
     */
    public static void PRINTF(final int level, final String format, Object... args) {
        if ((level&TRACE.GET_TRACE_LEVEL())!=0) {
            System.out.printf(format, args);
        }
    }
    
    static void SYSCALL(int number, Object... varargs) {
        String details=null;
        switch (number) {
            case SystemCall.MAKE_DEVICE:
                details=String.format("MAKE_DEVICE, id=%s,name=\"%s\"", varargs[0], varargs[1]);  
                break;
            case SystemCall.EXECVE:
                details=String.format("EXECVE, name=\"%s\"", varargs[0]);
                break;
            case SystemCall.IO_REQUEST:
                details=String.format("IO_REQUEST, %s, duration=%s, %s", varargs[0], varargs[1], varargs[2]);
                break;
            case SystemCall.TERMINATE_PROCESS:
                details=String.format("TERMINATE_PROCESS, %s", varargs[0]);
                break;
            default:
                details="ERROR_UNKNOWN_NUMBER";
            }
        TRACE.PRINTF(2, "Time: %010d Kernel: SysCall(%s)\n", Config.getSystemTimer().getSystemTime(), details);
    }

    static void SYSCALL_END() {
       TRACE.PRINTF(4, "Time: %010d Kernel: SysCall complete\n", Config.getSystemTimer().getSystemTime());
    }
    
    static void INTERRUPT(int interruptType, Object... varargs) {
        String details = null;
        switch (interruptType) {
            case InterruptHandler.TIME_OUT:
                details=String.format("TIME_OUT, %s", varargs[0]);  
                break;
            case InterruptHandler.WAKE_UP:
                details=String.format("WAKE_UP, %s, %s", varargs[0], varargs[1]);
               break;
            default:
                details="ERROR_UNKNOWN_NUMBER";
            }
        TRACE.PRINTF(8, "Time: %010d Kernel: Interrupt(%s)\n", Config.getSystemTimer().getSystemTime(), details);
    }

    static void INTERRUPT_END() {
        TRACE.PRINTF(16, "Time: %010d Kernel: Interrupt exit\n", Config.getSystemTimer().getSystemTime());
    }
}
