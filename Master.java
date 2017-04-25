import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Master implements IntMaster {

	private String ownIP;
	private String jobID;
	private IntMaster masterStub;

	private String inputFilename; // input
	private File outputFile; // output
	private boolean currentlyReading;
	private PrintWriter writer;

	// (worker stub, true), including own stub
	private HashMap<IntWorker, Boolean> workerStubs; 
	private ArrayList<String> workerIPs;
	private IntWorker ownWorkerStub;
	
	private HashMap<String, IntMapTask> mapperTasks; // the actual mapper tasks
	private HashMap<String, Boolean> mapperTasksDone; // whether a map is done
	private HashMap<String, IntReduceTask> reducerTasks;
	private HashMap<String, Boolean> reducerTasksDone;

	public Master(String myIP, String jid, String filename, ArrayList<String> workerIPs,
			HashMap<IntWorker, Boolean> workerStubs, IntWorker ownWorkerStub) {
		ownIP = myIP;
		jobID = jid;
		masterStub = null;
		
		inputFilename = filename;
		outputFile = new File("wordfrequencies.txt");
		currentlyReading = false;
		
		this.workerStubs = workerStubs;
		this.workerIPs = workerIPs;
		this.ownWorkerStub = ownWorkerStub;
		
		mapperTasks = new HashMap<String, IntMapTask>();
		mapperTasksDone = new HashMap<String, Boolean>();
		reducerTasks = new HashMap<String, IntReduceTask>();
		reducerTasksDone = new HashMap<String, Boolean>();
		
		// start Master stub


	}
	
	public void setStub(IntMaster stub) {
		this.masterStub = stub;
	}
	
	public void start() {
		try {
			this.writer = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	} 

}