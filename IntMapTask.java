import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntMapTask extends Remote {
	IntMapTask createMapTask(String jobID, String name) throws RemoteException, AlreadyBoundException;

	void processInput(String input, IntMaster theMaster) throws RemoteException, AlreadyBoundException;
}
