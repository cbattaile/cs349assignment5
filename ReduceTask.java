import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ReduceTask implements IntReduceTask {
	String key;
	IntMaster masterStub;
	int count;

	/*public ReduceTask() { // starting reducerMangaer
		this.key = "";
		this.masterStub = null;
		count = 0;
	}*/

	public ReduceTask(String key, IntMaster master) {
		this.key = key;
		this.masterStub = master;
		count = 0;
	}

/*	public IntReduceTask createReduceTask(String name, String key, IntMaster master) {
		ReduceTask reducerTask = new ReduceTask(key, master);
		// add to registry
		try {
			IntReduceTask reducerStub = (IntReduceTask) UnicastRemoteObject.exportObject(reducerTask, 0);
			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind(name, reducerStub);
			System.out.println("R: created Reducer " + name);
			return reducerStub;
		} catch (Exception e) {
			System.err.println("R: Client exception(could not register Reducer task " + key + "): \n" + e.toString());
			e.printStackTrace();
			return null;
		}

	}
*/
	public void receiveValues(int value) {
		System.out.println("R: " + key + " + " + value);
		count += value;
	}

	public int terminate() {
		// 9.once the reducer is done, it sends its results to the master, and
		// terminates
		System.out.println("R: terminating Reducer: " + key);
		try {
			System.out.println("R: Send to Master: " + key + " v: " + count);
			masterStub.receiveOutput(key, count);
			return 1;
		} catch (Exception e) {
			System.err.println("R: Master Exception - Cannot read file" + e.toString());
			e.printStackTrace();
			return -1;
		}
	}


}
