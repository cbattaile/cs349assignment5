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

		try {
			IntWorker worker = (IntWorker) UnicastRemoteObject.exportObject(worker, 0);
			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Worker", worker);
			sm.myWorker = worker;
			worker.initialize(workersRunning,ownIP);
			System.out.println("worker has been bound to RMI registry");
		} catch (Exception e) {
			System.err.println("Client exception (could not register worker): \n" + e.toString());
			e.printStackTrace();
		}

		// populate workersRunning array with worker stubs
		while (sm.workerIPs.size() != sm.workersRunning.size()) {
			for (int i = 0; i < workerIPs.size(); i++) {
				try {
					Registry reg = LocateRegistry.getRegistry(sm.workerIPs[i]);
					sm.workersRunning.put((IntWorker) reg.lookup("Worker"),true);
				} catch (Exception e) {
					System.out.println("Worker " + sm.workerIPs[i] + " not established yet...");
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
				sm.numJobs ++;
				IntMaster master = (IntMaster) UnicastRemoteObject.exportObject(master,0);
				registry.bind("Master " + sm.numJobs.toString(), master);
				master.beginJob(file,sm.workerIPs,sm.workersRunning,sm.ownIP);
			}
		}
	}
}