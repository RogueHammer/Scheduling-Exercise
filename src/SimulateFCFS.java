/**
 * Simulator for FCFS for OS Assignment 1
 *
 * @author Steven Mare	
 * @version 02 May 2018
 */
import java.util.Scanner;
import simulator.Kernel;
import simulator.Config;
import simulator.SystemTimer;
import simulator.TRACE;

public class SimulateFCFS{

	public static void main(String[] args){

		Scanner scan = new Scanner(System.in);

		System.out.println("*** FCFS Simulator ***");
		System.out.print("Enter configuration file name: ");
		String filename = scan.nextLine();
		System.out.print("Enter cost of system call: ");
		int syscost = scan.nextInt();
		System.out.print("Enter cost of context switch: ");
		int switchcost = scan.nextInt();
		System.out.print("Enter trace level: ");
		int tracelevel = scan.nextInt();

		final Kernel kernel = new FCFSKernel();
		TRACE.SET_TRACE_LEVEL(tracelevel);
		Config.init(kernel, switchcost, syscost);
		Config.buildConfiguration(filename);
		Config.run();
		SystemTimer timer = Config.getSystemTimer();
		System.out.println(timer);
		System.out.println("Context switches: " + Config.getCPU().getContextSwitches());
		System.out.printf("CPU utilization: %.2f\n", (double)timer.getUserTime()/timer.getSystemTime()*100);


	}


}