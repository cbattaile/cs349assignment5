import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Worker implements IntWorker {
	
public Worker() {
}

public IntMapTask startMapTask(String name, String input, IntMaster theMaster)
		throws RemoteException, AlreadyBoundException {
	MapTask mapTask = new MapTask(name);
	// add to registry
	IntMapTask mapStub; 
	try {
		mapStub = (IntMapTask) UnicastRemoteObject.exportObject(mapTask, 0);
		// Bind the remote object's stub in the registry
		Registry registry = LocateRegistry.getRegistry();
		registry.bind(name, mapStub);
		System.out.println("M: new MapTask for " + name);
		return mapStub;
	} catch (Exception e) {
		System.err.println("M: Client exception(could not register MapTask " + name + "): \n" + e.toString());
		e.printStackTrace();
		return null;
	}
}

public IntReduceTask startReduceTask(String name, String key, IntMaster theMaster)
		throws RemoteException, AlreadyBoundException {
	ReduceTask reducerTask = new ReduceTask(key, theMaster);
	// add to registry
	try {
		IntReduceTask reducerStub = (IntReduceTask) UnicastRemoteObject.exportObject(reducerTask, 0);
		// Bind the remote object's stub in the registry
		Registry registry = LocateRegistry.getRegistry();
		registry.bind(name, reducerStub);
		System.out.println("R: created Reducer " + name);
		return reducerStub;
	} catch (Exception e) {
		System.err.println("R: Client exception(could not register Reducer task for " + key + "): \n" + e.toString());
		e.printStackTrace();
		return null;
	}
}

}