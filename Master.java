public class Master implements IntMaster {

private String ownIP;
private HashMap<IntWorker,Boolean> workerStubs; //(worker stub, true), including own stub
private ArrayList<String> workerIPs;
private String file;
private String jobID;
private IntMaster stub;

public Master() {
	
}

public Master(String ownIP, HashMap<IntWorker,Boolean> workerStubs, ArrayList<String> workerIP) {

}

public void beginJob(String j, String f, ArrayList<String> ips, HashMap<IntWorker,Boolean> workers, String myIP) {
	ownIP = myIP;
	workerStubs = workers;
	workerIPs = ips;
	file = f;
	jobID = j;
}

public static void main(String[] args) {
	
}


public IntReduceTask[] getReducers(String[] keys) throws RemoteException, AlreadyBoundException {
	// TODO Auto-generated method stub
	return null;
}


public void markMapperDone(String mapperName) throws RemoteException {
	// TODO Auto-generated method stub
	
}


public void receiveOutput(String key, int value) throws RemoteException {
	// TODO Auto-generated method stub
	
}

}