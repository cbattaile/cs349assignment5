import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntMapTask extends Remote {
	void processInput(String input, IntMaster theMaster) throws RemoteException;
}
