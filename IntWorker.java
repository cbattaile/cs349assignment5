import java.rmi.Remote;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public interface IntWorker extends Remote {
	IntMapTask startMapTask(String name, String input, IntMaster theMaster) throws RemoteException, AlreadyBoundException;
	IntReduceTask startReduceTask(String name, String key, IntMaster theMaster) throws RemoteException, AlreadyBoundException;
}