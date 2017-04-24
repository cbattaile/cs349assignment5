import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;

public interface IntReduceTask extends Remote {
	IntReduceTask createReduceTask(String jobID, String key, IntMaster master) throws RemoteException, AlreadyBoundException;

	void receiveValues(int value) throws RemoteException;

	int terminate() throws RemoteException;
}
