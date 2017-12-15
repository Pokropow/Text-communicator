

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SInterface extends Remote
{
	int zapisz (KInterface u, String n) throws RemoteException;
	int wypisz(String n) throws RemoteException;
	String[] listuj() throws RemoteException;
	KInterface pobierz(String n) throws RemoteException;
}
