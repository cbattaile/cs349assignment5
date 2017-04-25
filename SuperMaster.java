import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class SuperMaster {

	private static final ArrayList<String> workerIPs = new ArrayList<String>(Arrays.asList("ownIP", "IP2", "IP3"));
	private HashMap<IntWorker,Boolean> workersRunning = new HashMap<IntWorker,Boolean>();
	private IntWorker myWorker;
	private String ownIP;
	private int numJobs;

	public SuperMaster() {
		ownIP = "";
		numJobs = 0;
	}

	public static void main (String [] args) {
		SuperMaster sm = new SuperMaster();

		sm.ownIP = args[0];
		Registry registry;
		// create a worker
		try {
			Worker worker = new Worker();
			IntWorker workerStub = (IntWorker) UnicastRemoteObject.exportObject(worker, 0);
			registry = LocateRegistry.getRegistry();
			registry.bind("Worker", worker);
			sm.myWorker = workerStub;
			System.out.println("worker has been bound to RMI registry");
		} catch (Exception e) {
			System.err.println("Client exception (could not register worker): \n" + e.toString());
			e.printStackTrace();
		}

		// populate workersRunning array with worker stubs
		while (sm.workerIPs.size() != sm.workersRunning.size()) {
			for (int i = 0; i < workerIPs.size(); i++) {
				try {
					Registry reg = LocateRegistry.getRegistry(sm.workerIPs.get(i));
					sm.workersRunning.put((IntWorker) reg.lookup("Worker"),true);
				} catch (Exception e) {
					System.out.println("Worker " + sm.workerIPs.get(i) + " not established yet...");
					e.printStackTrace();
				}
			}
		}

		// at this point, all workers are confirmed up and running
		// begin input loop checking for jobs coming in
		// in the form of .txt files

		Scanner scan = new Scanner(System.in);

		while (true) {
			if (scan.nextLine().equals("job")) {
				System.out.println("Enter txt file name:");
				String file = scan.nextLine();
				try {
					String jid = sm.ownIP.toString() + "_" + sm.numJobs;
					sm.numJobs ++;
					Master master = new Master(sm.ownIP, jid,file,sm.workerIPs,sm.workersRunning);
					IntMaster masterStub = (IntMaster) UnicastRemoteObject.exportObject(master,0);
					registry.bind("Master " + sm.numJobs, masterStub);
				} catch (Exception e) {
					System.out.println("Master " + sm.numJobs + " could not be started");
					e.printStackTrace();
				}
				
				
			}
		}
	}
}