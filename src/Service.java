
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
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
			System.out.printf("U¿ytkownik %s próbowa³ nawi¹zaæ po³¹czenie pod zajêt¹ nazw¹\n",n);
			return 0;
		}
		map.put(n, u);
		System.out.printf("U¿ytkownik %s po³¹czy³ siê\n",n);
		try {
			LocateRegistry.getRegistry().bind(n, u);
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
		u.pisz("Witaj "+n);
		for(KInterface clnt : map.values())
		{
			clnt.pisz("");
		}
		return 1;
	}

	@Override
	public int wypisz(String n) throws RemoteException {
		map.remove(n);
		for(KInterface clnt : map.values())
		{
			clnt.pisz("");
		}
		try {
			LocateRegistry.getRegistry().unbind(n);
			System.out.printf("U¿ytkownik %s roz³¹czy³ siê\n",n);
		} catch (NotBoundException e) {
			return 0;
		}
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
