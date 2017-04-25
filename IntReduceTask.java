import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntReduceTask extends Remote {
	void receiveValues(int value) throws RemoteException;

	int terminate() throws RemoteException;
}
