

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KInterface extends Remote
{
	void pisz (String s) throws RemoteException;
}
