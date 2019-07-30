package simulator;
import java.util.HashMap;
import java.util.Map;
//
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//
import java.util.Scanner;
/**
 * The class manages the configuration and running of a simulation.
 * <p>
 * It collates the hardware components: clock, cpu, and I/O devices.
 * Classes such as concrete Kernels can access these components here. 
 * 
 * @author Stephan Jamieson
 * @version 13/3/16
 */
public class Config {

    private Config() {}
    
    private static SimulationClock clock;
    private static EventScheduler scheduler;
    private static CPU cpu;
    private static Kernel kernel;
    private static Map<Integer, IODevice> devices = new HashMap<Integer, IODevice>();    
    
    static SimulationClock getSimulationClock() { return Config.clock; }
    static EventScheduler getEventScheduler() { return Config.scheduler; }
    static Kernel getKernel() { return kernel;}


	/**
	 * Obtain the SystemTimer used in the current configuration.
	 */
    public static SystemTimer getSystemTimer() { return Config.getSimulationClock(); }
	
	/**
	 * Obtain the CPU used in the current configuration.
	 */
    public static CPU getCPU() { return Config.cpu; }
    
	/**
	 * Add an IODevice to the configuration.
	 */
    public static void addDevice(IODevice device) { devices.put(device.getID(), device); }
	
	/** 
	 * Obtain the IODevice with the given ID.
	 */
    public static IODevice getDevice(int ID) { return devices.get(ID); }
    
	/**
	 * Create an initial simulation configuration that uses the given kernel, context switch cost and 
	 * system call cost.
	 */
    public static void init(Kernel kernel, int cSwitchCost, int sysCallCost) {
        Config.clock=new SimulationClock(sysCallCost, cSwitchCost);
        Config.scheduler=new EventScheduler();
        Config.cpu=new CPU();
        Config.kernel=kernel;
    }
    
	/** 
	 * Run the configured simulation (the init() and buildConfiguration() methods must be called first).
	 */
    public static void run() {
        Config.clock.setSystemTime(0);
        Config.scheduler.run();
	}
	   
    /**
	 * Complete the simulation configuration by uploading the given config file.
	 */
    public static void buildConfiguration(String filename) {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            String line = reader.readLine().trim();
            while (line!=null) {
                if (line.startsWith("#") || line.equals("")) {
                    // It's a commment or blank line, ignore.
                }
                else if (line.startsWith("PROGRAM")) {
                    Scanner scanner = new Scanner(line);
                    scanner.next(); // Consume "PROGRAM"
                    if (!scanner.hasNextInt()) {
                        // Whoops, missing program start time.
                        System.out.println("PROGRAM entry missing start time: \""+line+"\".");
                        System.exit(-1);
                    }
                    final int startTime = scanner.nextInt();
                    if (!scanner.hasNextInt()) {
                        // Whoops, priority.
                        System.out.println("PROGRAM entry missing priority: \""+line+"\".");
                        System.exit(-1);
                    }
                    final int priority = scanner.nextInt();
                    if (!scanner.hasNext()) {
                        // Whoops, missing program name.
                        System.out.println("PROGRAM entry missing program name: \""+line+"\".");
                        System.exit(-1);
                    }
                    Config.scheduler.schedule(new ExecveEvent(startTime, scanner.next(), priority, Config.getKernel())); 
                }
                else if (line.startsWith("DEVICE")) {
                    Scanner scanner = new Scanner(line);
                    scanner.next(); // Consume "DEVICE"
                    if (!scanner.hasNextInt()) {
                        // Whoops, missing device ID
                        System.out.println("DEVICE entry missing device ID: \""+line+"\".");
                        System.exit(-1);
                    }
                    final int deviceID = scanner.nextInt();
                    if (!scanner.hasNext()) {
                        // Whoops, missing device ID
                        System.out.println("DEVICE entry missing device type: \""+line+"\".");
                        System.exit(-1);
                    }
                    final String deviceType = scanner.next();
                    TRACE.SYSCALL(SystemCall.MAKE_DEVICE, deviceID, deviceType);
                    Config.getSimulationClock().logSystemCall();
                    Config.kernel.syscall(SystemCall.MAKE_DEVICE, deviceID, deviceType);
                    TRACE.SYSCALL_END();
                }
                else {
                    System.out.println("Unrecognised token in configuration file : \""+filename+"\".");
                    reader.close();
                    System.exit(-1);
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (FileNotFoundException fileNFExc) {
            System.out.println("File \""+filename+"\" not found.");
            System.exit(-1);
        }
        catch (IOException ioExc) {
            System.out.println("IO Error reading from \""+filename+"\".");
            System.exit(-1);
        }        
    }
}
