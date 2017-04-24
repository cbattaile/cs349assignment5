public interface IntWorker extends Remote {

private String ownIP;
private HashMap<IntWorker, Boolean> workerStubs; //(worker stub, true), including own stub

public IntWorker() {

}

public void initialize(HashMap workers, String myIP) {
	ownIP = myIP;
	workerStubs = workers;
}

}