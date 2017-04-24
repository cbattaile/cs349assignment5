public interface IntMaster extends Remote {

private String ownIP;
private HashMap<IntWorker,Boolean> workerStubs; //(worker stub, true), including own stub
private ArrayList<String> workerIPs;
private String file;

public IntMaster() {

}

public void beginJob(String f, ArrayList<String> ips, HashMap<IntWorker,Boolean> workers, String myIP) {
	ownIP = myIP;
	workerStubs = workers;
	workerIPs = ips;
	file = f;
}

}