import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Master implements IntMaster {

	private String jobID;
	private IntMaster masterStub;

	private String inputFilename; // input
	private File outputFile; // output
	private boolean currentlyReading;
	private PrintWriter writer;

	// (worker stub, true), including own stub
	private HashMap<IntWorker, Boolean> workerStubs;
	private IntWorker ownWorkerStub;
	private ArrayList<IntWorker> workerStubList; // arraylist of all non-self
													// worker stubs

	private HashMap<String, IntMapTask> mapperTasks; // the actual mapper tasks
	private HashMap<String, Boolean> mapperTasksDone; // whether a map is done
	private HashMap<String, IntReduceTask> reducerTasks;
	private HashMap<String, Boolean> reducerTasksDone;

	public Master(String jid, String filename, HashMap<IntWorker, Boolean> workerStubs, IntWorker ownWorkerStub) {
		jobID = jid;
		masterStub = null;

		inputFilename = filename;
		outputFile = new File("wordfrequencies.txt");
		currentlyReading = false;

		this.workerStubs = workerStubs;
		this.ownWorkerStub = ownWorkerStub;

		mapperTasks = new HashMap<String, IntMapTask>();
		mapperTasksDone = new HashMap<String, Boolean>();
		reducerTasks = new HashMap<String, IntReduceTask>();
		reducerTasksDone = new HashMap<String, Boolean>();
		workerStubs.remove(ownWorkerStub);
		workerStubList = new ArrayList<IntWorker>(workerStubs.keySet());

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
		try {
			currentlyReading = true;
			FileReader fr = new FileReader(inputFilename);
			BufferedReader bufr = new BufferedReader(fr);
			String line = bufr.readLine();
			int count = 0;
			int numWorkers = workerStubList.size();
			// 2. for each line, master starts task on one of the mapper nodes
			// ^^use FCFS
			while (line != null) {
				System.out.println("processing: " + line);
				// find a worker node and start a map task on it
				String mapTaskName = "M" + jobID + "_" + Integer.toString(count);
				IntMapTask newMapTask = workerStubList.get(count % numWorkers).startMapTask(mapTaskName, line,
						masterStub);
				mapperTasks.put(mapTaskName, newMapTask);
				mapperTasksDone.put(mapTaskName, false);
				newMapTask.processInput(line, masterStub);
				count++;
				line = bufr.readLine();
			}
			bufr.close();
			currentlyReading = false;

			// master checks that the mappers are done
			while (!mapperTasksDone.isEmpty()) {
				System.out.println("mappers not done");
			}
			synchronized (reducerTasks) {
				System.out.println("all mappers are done send terminate message to reducers");
				for (String reducerName : reducerTasks.keySet()) {
					IntReduceTask reducerStub = reducerTasks.get(reducerName);
					try {
						System.out.println("sending terminate to " + reducerName);
						reducerStub.terminate();
					} catch (Exception e) {
						System.err.println("ReducerTask Exception - could not terminate" + e.toString());
						e.printStackTrace();
					}
				}
			}

			// mapper checks that the reducers are done
			synchronized (reducerTasks) {
				if (mapperTasksDone.isEmpty() && reducerTasksDone.isEmpty() && !currentlyReading) {
					System.out.println("finished, closing and writing file");
					writer.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Master Exception - Cannot read file" + e.toString());
			e.printStackTrace();
		}

	}

	public IntReduceTask[] getReducers(String[] keys) {
		IntReduceTask[] matchingReducers = new IntReduceTask[keys.length + 1];
		for (int i = 0; i < keys.length; i++) {
			String reducerName = "R" + jobID + "_" + keys[i];
			if (reducerTasks.containsKey(reducerName)) {
				matchingReducers[i] = reducerTasks.get(reducerName);
			} else {
				// create new reducerTask
				IntWorker worker = workerStubList.get(reducerName.hashCode() % workerStubList.size());
				IntReduceTask newReduceTask;
				try {
					newReduceTask = worker.startReduceTask(reducerName, keys[i], masterStub);
					reducerTasks.put(reducerName, newReduceTask);
					reducerTasksDone.put(reducerName, false);
					matchingReducers[i] = newReduceTask;
				} catch (Exception e) {
					System.err.println("Client exception(could not add reduceTask): " + e.toString());
					e.printStackTrace();
				}

			}
		}
		return matchingReducers;
	}

	public void receiveOutput(String key, int value) {
		try {
			System.out.println("writing to file: " + key + ":" + value);
			writer.println(key + ":" + value);
			// writer.close();
			synchronized (reducerTasksDone) {
				reducerTasksDone.remove(key);
			}
		} catch (Exception e) {
			System.err.println("Master Exception - Cannot write file" + e.toString());
			e.printStackTrace();
		}

	}

	public void markMapperDone(String mapperName) {
		System.out.println("calling mark Master done, removing " + mapperName + " from " + mapperTasksDone.keySet());
		mapperTasksDone.remove(mapperName);
		System.out.println("calling mark Master done, removed " + mapperName + " from " + mapperTasksDone.keySet());
	}

}