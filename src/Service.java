
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class Service extends UnicastRemoteObject implements SInterface {	
	TreeMap<String,KInterface> map;
	static int port = 21029;
	public Service() throws RemoteException {		
		super(port);
		map = new TreeMap<String,KInterface>();
	}

	@Override
	public int zapisz(KInterface u, String n) throws RemoteException {
		//System.out.printf("Rejestracja nazwy \"%s\"\n",n);
		if(map.containsKey(n))
		{
			System.out.printf("Użytkownik %s próbował nawiązać połączenie pod zajętą nazwą\n",n);
			return 0;
		}
		map.put(n, u);
		System.out.printf("Użytkownik %s połączył się\n",n);
		/*try {
			LocateRegistry.getRegistry().bind(n, u);
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}*/
		u.pisz("Witaj "+n);
		pingClients();
		return 1;
	}
	
	void pingClients() throws RemoteException
	{
		for(String s : map.keySet())
		{
			KInterface clnt = map.get(s);
			try 
			{
				clnt.pisz("");
			} 
			catch (RemoteException e) 
			{
				map.remove(s);
			}
		}
	}

	@Override
	public int wypisz(String n) throws RemoteException {
		if(!map.containsKey(n))
		{
			return 0;
		}
		map.remove(n);
		pingClients();
		/*try {
			//LocateRegistry.getRegistry().unbind(n);
			System.out.printf("U�ytkownik %s roz��czy� si�\n",n);
		} catch (NotBoundException e) {
			return 0;
		}*/
		return 1;
	}

	@Override
	public String[] listuj() throws RemoteException {
		String[] list = new String[map.size()];		
		int i=0;
		for(String s : map.keySet())
		{
			list[i]=s;
			i++;
		}
		return list;
	}

	@Override
	public KInterface pobierz(String n) throws RemoteException {
		return map.get(n);
	}
}
