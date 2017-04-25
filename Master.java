import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Master implements IntMaster {

private String jid;
private String ownIP;
private HashMap<IntWorker,Boolean> workerStubs; //(worker stub, true), including own stub
private ArrayList<String> workerIPs;
private String jobID;
private IntMaster stub;
private String filename;
private File file;


public Master(String myIP, String jid, String filename, ArrayList<String> workerIPs, HashMap<IntWorker,Boolean> workerStubs) {
	ownIP = myIP;
	workerStubs = workerStubs;
	workerIPs = workerIPs;
	filename = filename;
	file = new File("wordfrequencies.txt");
	
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